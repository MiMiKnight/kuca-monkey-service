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
package(){
  local cmd="mvn clean compile package '-Dmaven.test.skip=true'";
  # 如果外部传入的maven配置文件变量不为空且文件存在
  if [ -z "${maven_setting_config}" ] && [ -f "${maven_setting_config}" ]; then
    cmd="${cmd} --settings='${maven_setting_config}'"
  fi
  #mvn clean compile package '-Dmaven.test.skip=true' --settings="xxx/jdk8-settings.xml"
  #mvn clean compile package '-Dmaven.test.skip=true' --settings="xxx/jdk17-settings.xml"
  su -c "${cmd}"

  # 循环等待打包结束
  local timeout now start_time end_time duration;
  timeout=300; # 打包超时时间（单位：秒）
  now=$(date +'%Y-%m-%d %H:%M:%S');
  start_time=$(date --date="${now}" +%s);
  end_time=${start_time}
  duration=0 # 持续时间
  until [ ! -d "${parent_dir}/.build" ]
  do
    echo "[TIP] maven is packaging now ...."
    now=$(date +'%Y-%m-%d %H:%M:%S')
    end_time=$(date --date="$now" +%s);
    duration=${end_time}-${start_time}
    if [ ${duration} -ge ${timeout} ]; then
      echo "[ERROR] maven package timeout !!!"
      exit 1 # 超时则报错退出脚本执行
    fi
    sleep 1 # 每秒执行一次
  done
  echo "[TIP]maven package finish!!!"
}
# 执行项目打包
package

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
# 项目镜像名
image_name="${image_domain}/${image_library}/${app_name}:${app_version}"

#####################################
## 构建镜像函数
#####################################
build_image(){
 # 进入Dockerfile文件所在的同级目录
 cd "${parent_dir}/.build"
 # 构建docker镜像
 sudo docker build \
  --file "${parent_dir}/.build/Dockerfile" \
  --tag "${image_name}" .
 # 回到父级目录
 cd "${parent_dir}"
 # 登陆docker
 sudo docker login ${image_domain} --username ${image_user} --password ${image_password}
 # 上传docker镜像
 sudo docker push "${image_name}"
 # 退出登陆docker
 sudo docker logout
}

# 执行镜像构建
build_image
# 执行清除构建内容
sudo rm -rf "${parent_dir}/.build"