#!/bin/bash
set -eu
# set -x 参数 作用：显示参数值（调试脚本时打开，平时注释）
#################################
## build.sh
## 描述：项目打包构建脚本
## $1: 镜像仓库域名
## $2: 镜像仓库用户名
## $3: 镜像仓库用户密码
## $4: 镜像仓库指定库名
#################################
# sudo apt-get install -y jq
# sudo apt-get install pwgen

##############全局变量##############
# 脚本当前所在目录
script_current_dir=$(cd "$(dirname "$0")" && pwd)
# 项目目录
app_dir=$(dirname "$script_current_dir")
# 镜像仓库域名
image_domain=$1
# 镜像仓库用户名
image_user=$2
# 镜像仓库密码
image_password=$3
# 镜像仓库名
image_library=$4


##################################
# 友好提示函数
##################################
Info() {
  now=$(date +'%Y-%m-%d %H:%M:%S')
  echo -e "\e[1;90;49m[${now}] \e[1;32;49m[INFO] \e[1;39;49m$1\e[0m";
}

##################################
# 警告提示函数
##################################
Warn() {
  now=$(date +'%Y-%m-%d %H:%M:%S')
  echo -e "\e[1;90;49m[${now}] \e[1;33;49m[WARN] \e[1;39;49m$1\e[0m";
}

##################################
# 错误提示退出函数
##################################
Error() {
  now=$(date +'%Y-%m-%d %H:%M:%S')
  echo -e "\e[1;90;49m[${now}] \e[1;31;49m[ERROR] \e[1;39;49m$1\e[0m";
  exit 1
}

#####################################
## Check Java 函数
#####################################
CheckJava(){
  local java_location="${JAVA_HOME}/bin/java"
  if [ ! -e "${java_location}" ] || [ ! -x "${java_location}" ]; then
     Error "Please install Java and set environment variables or check it !!!"
  fi
}

#####################################
## Check Maven 函数
#####################################
CheckMaven(){
  CheckJava
  local mvn_location="${MAVEN_HOME}/bin/mvn"
  if [ ! -e "${mvn_location}" ] || [ ! -x "${mvn_location}" ]; then
     Error "Please install Maven and set environment variables or check it !!!"
  fi
}

#####################################
## maven package 函数
#####################################
MavenPackage(){
  # 检查Maven
  CheckMaven
  # 构建打包命令
  local project_dir="" current_dir=""
  project_dir=${app_dir}
  # 记录当前目录
  current_dir=$(pwd)
  # 切换到项目目录
  cd "${project_dir}" || exit  1

  local cmd="${JAVA_HOME}/bin/java \
  -Dmaven.multiModuleProjectDirectory=${app_dir} \
  -Dmaven.home=${MAVEN_HOME} \
  -Dclassworlds.conf=${MAVEN_HOME}/bin/m2.conf \
  -Dfile.encoding=UTF-8 \
  -classpath ${MAVEN_HOME}/boot/plexus-classworlds-2.7.0.jar:${MAVEN_HOME}/boot/plexus-classworlds.license \
  org.codehaus.classworlds.Launcher \
  --settings ${MAVEN_HOME}/conf/settings.xml \
  -DskipTests=true \
  clean compile package"

  # 执行打包命令
  eval "${cmd}"
  #eval "${cmd}" > /dev/null 2>&1 &
  # 切换到回原有的目录下
  cd "${current_dir}" || exit  1

  # 循环等待打包结束
  local timeout now start_time end_time duration;
  timeout=300; # 打包超时时间（单位：秒）
  now=$(date +'%Y-%m-%d %H:%M:%S');
  start_time=$(date --date="${now}" +%s);
  end_time=${start_time}
  duration=0 # 持续时间
  # 构建产物不存在时则等待构建，执行循环体；构建产物存在，则跳出循环；
  local metadata_file_location="${app_dir}/.build/deployment/metadata.json"
  while [ ! -f "${metadata_file_location}" ];
  do
    Info "maven is packaging project now ...."
    now=$(date +'%Y-%m-%d %H:%M:%S')
    end_time=$(date --date="$now" +%s);
    duration=$((end_time-start_time))
    if [ ${duration} -gt ${timeout} ]; then
      # 超时则报错退出脚本执行
      Error "maven package project timeout !!!"
    fi
    sleep 2 # 循环每2秒执行一次
  done
  Info "maven package finish!!!"
}
MavenPackage

#####################################
## dos2unix 函数
#####################################
FileDos2Unix(){
  if [ ! -d "${app_dir}/.build" ]; then
    Warn "${app_dir}/.build not exist !!!"
    exist 1
  fi
  find "${app_dir}/.build" -type f -print0 | xargs -0 dos2unix -k -s
}

#####################################
## move file 函数
#####################################
MoveFile(){
  if [ ! -d "${app_dir}/.build/deployment" ];then
    Warn "${app_dir}/.build/deployment not exist !!!"
    exist 1
  fi
  sudo cp -f ${app_dir}/.build/deployment/* ${app_dir}/.build/
  sudo rm -rf "${app_dir}/.build/deployment"
  FileDos2Unix
}
MoveFile

#####################################
## 生成构建版本 函数
#####################################
BuildVersion(){
  # 生成构建版本
  local build_version="" cmd="";
  build_version="$(date +%Y%m%d%H%M%S)$(pwgen -ABns0 8 1 | tr a-z A-Z)"
  # 写入项目构建版本信息
  echo "$(jq --arg value ${build_version} '.APP_BUILD_VERSION = $value' ${app_dir}/.build/metadata.json)" > ${app_dir}/.build/metadata.json
}
BuildVersion

# 项目名称
C_APP_NAME="$(jq -r '.APP_NAME' ${app_dir}/.build/metadata.json)"
# 项目构建版本
C_APP_BUILD_VERSION="$(jq -r '.APP_BUILD_VERSION' ${app_dir}/.build/metadata.json)"
# 项目镜像坐标
C_IMAGE_COORDINATE="${image_domain}/${image_library}/${C_APP_NAME}:${C_APP_BUILD_VERSION}"

#####################################
## 写入元数据 函数
#####################################
WriteMetadata(){
  # 写入项目镜像坐标信息
  echo "$(jq --arg value ${C_IMAGE_COORDINATE} '.APP_IMAGE_COORDINATE = $value' ${app_dir}/.build/metadata.json)" > ${app_dir}/.build/metadata.json
}
WriteMetadata

#####################################
## 构建镜像函数
#####################################
BuildImage(){
 # 进入Dockerfile文件所在的同级目录
 cd "${app_dir}/.build" || exit 1
 # 构建docker镜像
 sudo docker build \
  --file "${app_dir}/.build/Dockerfile" \
  --build-arg build_version="${C_APP_BUILD_VERSION}" \
  --build-arg timezone="Asia/Shanghai" \
  --tag "${C_IMAGE_COORDINATE}" .
 # 回到父级目录
 cd "${app_dir}" || exit 1
 # 登陆docker
 sudo docker login ${image_domain} --username ${image_user} --password ${image_password}
 # 上传docker镜像
 sudo docker push "${C_IMAGE_COORDINATE}"
 # 删除产物镜像
 sudo docker rmi "$(sudo docker images | grep "${C_APP_BUILD_VERSION}" | grep "${image_domain}/${image_library}/${C_APP_NAME}" | awk '{print $3}')"
 # 退出登陆docker
 sudo docker logout
}
BuildImage

#####################################
## 构建K8S部署 函数
#####################################
BuildBlueprint(){
  Info "Start build blueprint !!!"
  # 替换镜像坐标
  sed -i "s@image_coordinate:tag@${C_IMAGE_COORDINATE}@g" "${app_dir}/.build/blueprint.yaml"
}
BuildBlueprint

#####################################
## 生成部署包 函数
#####################################
BuildDeployPackage(){
  local current_dir="" archive_name="";
  # 记录当前目录
  current_dir=$(pwd)
  # 切换到.build目录
  cd "${app_dir}/.build" || exit 1
  # 压缩包名
  archive_name="deploy-${C_APP_NAME}-${C_APP_BUILD_VERSION}.tar.gz"
  # 生成部署压缩包
  tar czf "${archive_name}" blueprint.yaml metadata.json
  # 将部署包拷贝到"部署文件夹"
  cp -f "${app_dir}/.build/${archive_name}" "$(dirname $app_dir)"
  # 在"部署文件夹"生成deploy.json
  local json_txt="{\"DEPLOY_PACKAGE_NAME\":\"${archive_name}\"}"
  echo "${json_txt}" >> "$(dirname $app_dir)/deploy.json"
  # 切换到回原有的目录下
  cd "${current_dir}" || exit 1
}
BuildDeployPackage

#
rm -rf "${app_dir}/.build"
Info "The project has been built completed and success !!!"