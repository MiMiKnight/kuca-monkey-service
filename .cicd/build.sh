#!/bin/bash
set -euE
# set -x 参数 作用：显示参数值（调试脚本时打开，平时注释）
#################################
## build.sh
## 描述：项目打包构建脚本
## $1: 镜像仓库域名
## $2: 镜像仓库命名空间
## $3: 产物生成的目标目录
## $4: 项目源码根目录
#################################
# sudo apt-get install -y jq
# sudo apt-get install pwgen

##############全局变量##############
# 镜像仓库域名
image_domain=$1
# 镜像仓库命名空间
image_namespace=$2
# 构建产物的存放目录
product_dir=$3
# 项目源码根目录
project_dir=$4
# 脚本当前所在目录
script_current_dir=$(cd "$(dirname "$0")" && pwd)
# 包生成目录
package_dir="${project_dir}/.build"
#  deployment.tar.gz 包文件名
deployment_package_file_name="deployment.tar.gz"
#  deployment.tar.gz 包文件位置
deployment_package_file_location="${package_dir}/${deployment_package_file_name}"
# 项目名称
app_name=""
# 项目构建版本
app_build_version=""
# 项目镜像坐标
image_coordinate=""
# 归档文件产物名称规则
product_archive_name_regular="deploy-{{name}}.tar.gz"
# JSON产物文件名
product_json_file_name="deploy.json"
# JSON产物文件内容格式
product_json_file_content_regular="{\"DEPLOY_PACKAGE_NAME\":\"{{content}}\"}"



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
}

#####################################
## Check Arg 函数
#####################################
CheckArg(){
  if [ -z "${image_domain}" ]; then
      Error "Arg: 'image_domain' value invalid !!!"
      exit 1
  fi
  if [ -z "${image_namespace}" ]; then
      Error "Arg: 'image_namespace' value invalid !!!"
      exit 1
  fi
  if [ -z "${product_dir}" ]; then
      Error "Arg: 'product_dir' value invalid !!!"
      exit 1
  fi
  if [ -z "${project_dir}" ] && [ -d "${project_dir}" ]; then
      Error "Arg: 'project_dir' value invalid !!!"
      exit 1
  fi
}

#####################################
## Check Java 函数
#####################################
CheckJava(){
  local java_location="${JAVA_HOME}/bin/java"
  if [ ! -e "${java_location}" ] || [ ! -x "${java_location}" ]; then
     Error "please install Java and set environment variables or check it !!!"
     exit 1
  fi
}

#####################################
## Check Maven 函数
#####################################
CheckMaven(){
  CheckJava
  local mvn_location="${MAVEN_HOME}/bin/mvn"
  if [ ! -e "${mvn_location}" ] || [ ! -x "${mvn_location}" ]; then
     Error "please install Maven and set environment variables or check it !!!"
     exit 1
  fi
}

#####################################
## maven package 函数
#####################################
MavenPackage(){
  # 检查Maven
  CheckMaven
  # 构建打包命令
  local project_dir=${project_dir} current_dir=$(pwd)
  # 切换到项目目录
  cd "${project_dir}" || exit  1

  local cmd="${JAVA_HOME}/bin/java \
  -Dmaven.multiModuleProjectDirectory=${project_dir} \
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
  # 构建包不存在时则等待构建，执行循环体；构建包存在，则跳出循环；
  while [ ! -f "${deployment_package_file_location}" ];
  do
    Info "maven is packaging project now ...."
    now=$(date +'%Y-%m-%d %H:%M:%S')
    end_time=$(date --date="$now" +%s);
    duration=$((end_time-start_time))
    if [ ${duration} -gt ${timeout} ]; then
      # 超时则报错退出脚本执行
      Error "maven package project timeout !!!"
      exit 1
    fi
    sleep 2 # 循环每2秒执行一次
  done
  Info "maven package finish!!!"
}

#####################################
## dos2unix 函数
#####################################
FileDos2Unix(){
  local dir=$1
  if [ ! -d "${dir}" ]; then
    Error "${dir} not exist !!!"
    exit 1
  fi
  find "${dir}" -type f -print0 | xargs -0 dos2unix -k -s
}

#####################################
## 检查生成包文件 函数
#####################################
CheckPackageFile(){
  Info "start check package file !!!"
  # 检查deployment.tar.gz包是否存在
  if [ ! -f "${deployment_package_file_location}" ];then
    Error "${deployment_package_file_location} not exist !!!"
    exit 1
  fi
  # 解压
  tar -xf "${deployment_package_file_location}" -C "${package_dir}"
  sudo rm -rf "${deployment_package_file_location}"
  # 文本文件dos转unix操作
  FileDos2Unix "${package_dir}"

  # 检查blueprint.yaml文件是否存在
  if [ ! -f "${package_dir}/blueprint.yaml" ]; then
      Error "${package_dir}/blueprint.yaml file not exit !!!"
      exit 1
  fi
  # 检查blueprint.yaml文件的yaml文件格式是否正确（若格式有误，则此语句会报错）
  yq -P -oj "${package_dir}/blueprint.yaml" >> "${package_dir}/blueprint.json"

  # 检查metadata.yaml文件是否存在
  if [ ! -f "${package_dir}/metadata.yaml" ]; then
       Error "${package_dir}/metadata.yaml file not exit !!!"
       exit 1
  fi
  # 检查metadata.yaml文件的yaml文件格式是否正确（若格式有误，则此语句会报错）
  yq -P -oj "${package_dir}/metadata.yaml" >> "${package_dir}/metadata.json"

  # 读取项目归档文件名称
  local app_archive_name="" app_archive_location=""
  app_archive_name=$(yq '.APP_ARCHIVE_NAME' "${package_dir}/metadata.yaml")
  # 检查项目归档文件是否存在
  app_archive_location="${package_dir}/${app_archive_name}"
  if [ ! -f "${app_archive_location}" ]; then
    Error "${app_archive_name} not exist !!!"
    exit 1
  fi
  Info "check package file finished !!!"
}

#####################################
## 写入构建版本 函数
#####################################
WriteBuildVersion(){
  # 生成构建版本
  local build_version="" cmd="";
  build_version="$(date +%Y%m%d%H%M%S)$(pwgen -ABns0 8 1 | tr a-z A-Z)"
  # 写入项目构建版本信息
  #echo "$(jq --arg value ${build_version} '.APP_BUILD_VERSION = $value' ${project_dir}/.build/metadata.json)" > ${project_dir}/.build/metadata.json
  yq -i ".APP_BUILD_VERSION=\"${build_version}\"" "${package_dir}/metadata.yaml"
  Info "build version = '${build_version}'"
}

#####################################
## ReadMetadata 函数
#####################################
ReadMetadata(){
  # 项目名称
  app_name=$(yq '.APP_NAME' "${package_dir}/metadata.yaml")
  # 项目构建版本
  app_build_version=$(yq '.APP_BUILD_VERSION' "${package_dir}/metadata.yaml")
  # 项目镜像坐标
  image_coordinate="${image_domain}/${image_namespace}/${app_name}:${app_build_version}"
}

#####################################
## WriteMetadata 函数
#####################################
WriteMetadata(){
  # 写入项目镜像坐标
  yq -i ".IMAGE_COORDINATE=\"${image_coordinate}\"" "${package_dir}/metadata.yaml"
  # 写入项目镜像域名
  yq -i ".IMAGE_DOMAIN=\"${image_domain}\"" "${package_dir}/metadata.yaml"
}

#####################################
## 构建镜像 函数
#####################################
BuildImage(){
  Info "start build application image !!!"
  local current_dir=$(pwd)
  # 切换到构建包生成目录下
  cd "${package_dir}" || exit 1
  # 构建docker镜像
  sudo docker build \
   --file "${package_dir}/Dockerfile" \
   --build-arg build_version="${app_build_version}" \
   --build-arg timezone="Asia/Shanghai" \
   --tag "${image_coordinate}" .
  cd "${current_dir}" || exit 1
  # 上传docker镜像
  sudo docker push "${image_coordinate}"
  Info "push image success ,coordinate = '${image_coordinate}'"
  # 删除本地产物镜像
  sudo docker rmi "$(sudo docker images | grep "${app_build_version}" | grep "${image_domain}/${image_namespace}/${app_name}" | awk '{print $3}')"
  Info "build app images finished and success !!!"
}

#####################################
## 生成部署包 函数
#####################################
BuildDeployPackage(){
  Info "start build deploy package !!!"
  local current_dir=$(pwd) product_archive_name="";
  # 产物归档文件名称
  # See if you can use ${variable//search/replace} instead.
  product_archive_name=${product_archive_name_regular//"{{name}}"/"${app_name}-${app_build_version}"}
  # 切换到构建包生成目录下
  cd "${package_dir}" || exit 1
  # 生成产物归档文件
  tar czf "${product_archive_name}" blueprint.yaml metadata.yaml
  # 切换到回原有的目录下
  cd "${current_dir}" || exit 1
  # 将部署包拷贝到"产物目录"
  cp -f "${package_dir}/${product_archive_name}" "${product_dir}"
  # 在"产物目录"下生成deploy.json
  echo "${product_json_file_content_regular//"{{content}}"/"${product_archive_name}"}" >> "${product_dir}/${product_json_file_name}"
  Info "build deploy package finished and success !!!"
}

#####################################
## Clean 函数
#####################################
Clean(){
  # 删除构建文件夹
  local dir="${package_dir}"
  if [ -d "${dir}" ]; then
      rm -rf "${dir}"
  fi
  Info "clean build package product"
}

#####################################
## Run 函数
#####################################
Run(){
  CheckArg
  CheckJava
  CheckMaven
  MavenPackage
  CheckPackageFile
  WriteBuildVersion
  ReadMetadata
  WriteMetadata
  BuildImage
  BuildDeployPackage
}
Run