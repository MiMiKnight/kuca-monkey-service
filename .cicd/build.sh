#!/bin/bash
set -ex
# 脚本当前所在目录
current_dir=$(cd "$(dirname "$0")" && pwd)
# 当前脚本所在目录的上一级目录
parent_dir=$(dirname "$current_dir")

# maven配置文件路径
maven_setting_config=$1

#####################################
## maven package 函数
#####################################
maven_package(){
  local cmd="mvn clean compile package '-Dmaven.test.skip=true'";
  # 如果外部传入的maven配置文件变量不为空且文件存在
  if [ -z "${maven_setting_config}" ] && [ -f "${maven_setting_config}" ]; then
    cmd="${cmd} --settings='${maven_setting_config}'"
  fi
  #mvn clean compile package '-Dmaven.test.skip=true' --settings="xxx/jdk8-settings.xml"
  #mvn clean compile package '-Dmaven.test.skip=true' --settings="xxx/jdk17-settings.xml"
  # 执行打包命令
  `${cmd}`

  # 循环等待打包结束
  local timeout now start_time end_time duration;
  timeout=300; # 打包超时时间（单位：秒）
  now=$(date +'%Y-%m-%d %H:%M:%S');
  start_time=$(date --date="${now}" +%s);
  end_time=${start_time}
  duration=0 # 持续时间
  # 构建产物不存在时则等待构建，执行循环体；构建产物存在，则跳出循环；
  until [ -d "${parent_dir}/.build" ]
  do
    echo "[TIP] maven is packaging project now ...."
    now=$(date +'%Y-%m-%d %H:%M:%S')
    end_time=$(date --date="$now" +%s);
    duration=$((${end_time}-${start_time}))
    if [ ${duration} -gt ${timeout} ]; then
      echo "[ERROR] maven package project timeout !!!"
      exit 1 # 超时则报错退出脚本执行
    fi
    sleep 2 # 循环每2秒执行一次
  done
  echo "[TIP]maven package finish!!!"
}
# 执行maven打包操作
maven_package

#####################################
## dos2unix 函数
#####################################
file_dos2unix(){
  if [ ! -d "${parent_dir}/.build" ]; then
    echo "[TIP] ${parent_dir}/.build not exist!!!"
    exist 0
  fi
 sudo find "${parent_dir}/.build" -type f -print0 | xargs -0 dos2unix -k -s
}

#####################################
## move file 函数
#####################################
move_file(){
  if [ ! -d "${parent_dir}/.build/deployment" ];then
    echo "[TIP] ${parent_dir}/.build/deployment not exist!!!"
    exist 0
  fi
  sudo mv -f ${parent_dir}/.build/deployment/* ${parent_dir}/.build/
  sudo rm -rf "${parent_dir}/.build/deployment"
  file_dos2unix
}
# 执行 move file 函数
move_file

# 项目名称
app_name="$(awk -F '=' 'NR==2{print $2}' "${parent_dir}/.build/metadata.txt")"
# 项目版本
app_version="$(awk -F '=' 'NR==4{print $2}' "${parent_dir}/.build/metadata.txt")"
# 镜像仓库用户名
image_user="mmk"
# 镜像仓库密码
image_password="Harbor12345"
# 镜像仓库域名
image_domain="harbor.devops.vm.mimiknight.cn"
# 镜像仓库名
image_library="mmkd"
# 项目镜像标签
image_tag="${image_domain}/${image_library}/${app_name}:${app_version}"

#####################################
## 构建镜像函数
#####################################
build_image(){
 # 进入Dockerfile文件所在的同级目录
 cd "${parent_dir}/.build"
 # 构建docker镜像
 sudo docker build \
  --file "${parent_dir}/.build/Dockerfile" \
  --tag "${image_tag}" .
 # 回到父级目录
 cd "${parent_dir}"
 # 登陆docker
 sudo docker login ${image_domain} --username ${image_user} --password ${image_password}
 # 上传docker镜像
 sudo docker push "${image_tag}"
 # 删除产物镜像
 sudo docker rmi "$(sudo docker images | grep "${app_version}" | grep "${image_domain}/${image_library}/${app_name}" | awk '{print $3}')"
 # 退出登陆docker
 sudo docker logout
}
# 执行镜像构建
build_image

#####################################
## 构建K8S部署 函数
#####################################
build_blueprint(){
  echo "build_blueprint"
  # 替换镜像地址
  sed -i "s@{{image_tag}}@${image_tag}@g" ${parent_dir}/.build/blueprint.yaml
  # 向k8s推送部署服务
}
build_blueprint

# 执行清除构建内容
sudo rm -rf "${parent_dir}/.build"