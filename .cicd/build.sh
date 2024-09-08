#!/bin/bash
set -ex
#################################
## 描述：项目打包构建脚本
## $1 maven配置文件路径
## jq工具，apt-get install -y jq
#################################
#sudo apt-get install -y jq

############全局变量常量############
# 脚本当前所在目录 常量
CONST_CURRENT_DIR=$(cd "$(dirname "$0")" && pwd)
declare -r CONST_CURRENT_DIR;
# 脚本所在的上一级目录 常量
CONST_PARENT_DIR=$(dirname "$CONST_CURRENT_DIR")
declare -r CONST_PARENT_DIR;
# maven配置文件路径 常量
CONST_MAVEN_SETTING_LOCATION=$1
declare -r CONST_MAVEN_SETTING_LOCATION;

#####################################
## maven package 函数
#####################################
MavenPackage(){
  local cmd="mvn clean compile package '-Dmaven.test.skip=true'";
  # 如果外部传入的maven配置文件变量不为空且文件存在
  if [ -z "${CONST_MAVEN_SETTING_LOCATION}" ] && [ -f "${CONST_MAVEN_SETTING_LOCATION}" ]; then
    cmd="${cmd} --settings='${CONST_MAVEN_SETTING_LOCATION}'"
  fi
  #mvn clean compile package '-Dmaven.test.skip=true' --settings="xxx/jdk8-settings.xml"
  #mvn clean compile package '-Dmaven.test.skip=true' --settings="xxx/jdk17-settings.xml"
  # 执行打包命令
  `${cmd}`

  # 循环等待打包结束
  local -i timeout now start_time end_time duration;
  timeout=300; # 打包超时时间（单位：秒）
  now=$(date +'%Y-%m-%d %H:%M:%S');
  start_time=$(date --date="${now}" +%s);
  end_time=${start_time}
  duration=0 # 持续时间
  # 构建产物不存在时则等待构建，执行循环体；构建产物存在，则跳出循环；
  until [ -d "${CONST_PARENT_DIR}/.build" ]
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
# 执行maven打包构建操作
MavenPackage

#####################################
## dos2unix 函数
#####################################
FileDos2Unix(){
  if [ ! -d "${CONST_PARENT_DIR}/.build" ]; then
    echo "[TIP] ${CONST_PARENT_DIR}/.build not exist!!!"
    exist 0
  fi
 sudo find "${CONST_PARENT_DIR}/.build" -type f -print0 | xargs -0 dos2unix -k -s
}

#####################################
## move file 函数
#####################################
MoveFile(){
  if [ ! -d "${CONST_PARENT_DIR}/.build/deployment" ];then
    echo "[TIP] ${CONST_PARENT_DIR}/.build/deployment not exist!!!"
    exist 0
  fi
  sudo cp -f ${CONST_PARENT_DIR}/.build/deployment/* ${CONST_PARENT_DIR}/.build/
  #sudo rm -rf "${CONST_PARENT_DIR}/.build/deployment"
  FileDos2Unix
}
# 执行 move file 函数
MoveFile

#####################################
## 生成构建版本 函数
#####################################
BuildVersion(){
  # 生成构建版本
  local build_version="" cmd=""
  build_version="$(date +%Y%m%d%H%M%S%N)"
  # 向JSON文件写入构建版本
  echo "$(jq --arg value ${build_version} '.APP_BUILD_VERSION = $value' ${CONST_PARENT_DIR}/.build/metadata.json)" > ${CONST_PARENT_DIR}/.build/metadata.json
}
 # 生成构建版本
 BuildVersion

# 项目名称
app_name="$(jq -r '.APP_NAME' ${CONST_PARENT_DIR}/.build/metadata.json)"
# 项目构建版本
app_build_version="$(jq -r '.APP_BUILD_VERSION' ${CONST_PARENT_DIR}/.build/metadata.json)"
# 镜像仓库用户名
image_user="mmk"
# 镜像仓库密码
image_password="Harbor12345"
# 镜像仓库域名
image_domain="harbor.devops.vm.mimiknight.cn"
# 镜像仓库名
image_library="mmkd"
# 项目镜像坐标
image_coordinate="${image_domain}/${image_library}/${app_name}:${app_build_version}"

#####################################
## 构建镜像函数
#####################################
BuildImage(){
 # 进入Dockerfile文件所在的同级目录
 cd "${CONST_PARENT_DIR}/.build"
 # 构建docker镜像
 sudo docker build \
  --file "${CONST_PARENT_DIR}/.build/Dockerfile" \
  --build-arg build_version="${app_build_version}" \
  --build-arg timezone="Asia/Shanghai" \
  --tag "${image_coordinate}" .
 # 回到父级目录
 cd "${CONST_PARENT_DIR}"
 # 登陆docker
 sudo docker login ${image_domain} --username ${image_user} --password ${image_password}
 # 上传docker镜像
 sudo docker push "${image_coordinate}"
 # 删除产物镜像
 sudo docker rmi "$(sudo docker images | grep "${app_build_version}" | grep "${image_domain}/${image_library}/${app_name}" | awk '{print $3}')"
 # 退出登陆docker
 sudo docker logout
}
# 执行镜像构建
BuildImage

#####################################
## 构建K8S部署 函数
#####################################
BuildBlueprint(){
  echo "build blueprint"
  # 替换镜像地址
  sed -i "s@{{image_coordinate}}@${image_coordinate}@g" ${CONST_PARENT_DIR}/.build/blueprint.yaml
  # 向k8s推送部署服务
}
BuildBlueprint

# 执行清除构建内容
#sudo rm -rf "${CONST_PARENT_DIR}/.build"