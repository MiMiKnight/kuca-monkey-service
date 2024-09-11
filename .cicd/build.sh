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
# 程序锁文件路径
lock_file_location="${script_current_dir}/build-bohpdqmvyxoflyqt310u.lock"
# 项目目录
app_dir=$(dirname "$script_current_dir")
# 镜像仓库域名
image_domain=$1
# 镜像仓库名
image_library=$2
# 构建产物的存放目录
product_dir=$3
# 项目名称
app_name=""
# 项目构建版本
app_build_version=""
# 项目镜像坐标
image_coordinate=""



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
## lock 函数
#####################################
Lock(){
  if [ -f "${lock_file_location}" ]; then
     Warn "there are already running build task. please try again later !!!"
     # 脚本退出执行
     exit 0
  else
     # 创建锁文件
     touch "${lock_file_location}"
     chattr +i "${lock_file_location}"
  fi
}

#####################################
## unlock 函数
#####################################
Unlock(){
  if [ -f "${lock_file_location}" ]; then
     chattr -i "${lock_file_location}"
     rm -rf "${lock_file_location}"
     exit 0
  fi
}

#####################################
## trace error 函数
## 显示错误位置，打印错误内容
#####################################
TraceError(){
  Warn "script name: $0 ,error on line $1 ,command: '$2'"
  exit 0
}

#####################################
## Check Java 函数
#####################################
CheckJava(){
  local java_location="${JAVA_HOME}/bin/java"
  if [ ! -e "${java_location}" ] || [ ! -x "${java_location}" ]; then
     Error "please install Java and set environment variables or check it !!!"
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

#####################################
## 写入构建版本 函数
#####################################
WriteBuildVersion(){
  # 生成构建版本
  local build_version="" cmd="";
  build_version="$(date +%Y%m%d%H%M%S)$(pwgen -ABns0 8 1 | tr a-z A-Z)"
  # 写入项目构建版本信息
  echo "$(jq --arg value ${build_version} '.APP_BUILD_VERSION = $value' ${app_dir}/.build/metadata.json)" > ${app_dir}/.build/metadata.json
  Info "build version = '${build_version}'"
}

#####################################
## BuildInfo 函数
#####################################
BuildInfo(){
 # 项目名称
 app_name="$(jq -r '.APP_NAME' ${app_dir}/.build/metadata.json)"
 # 项目构建版本
 app_build_version="$(jq -r '.APP_BUILD_VERSION' ${app_dir}/.build/metadata.json)"
 # 项目镜像坐标
 image_coordinate="${image_domain}/${image_library}/${app_name}:${app_build_version}"
}

#####################################
## 写入元数据 函数
#####################################
WriteMetadata(){
  # 写入项目镜像坐标信息
  echo "$(jq --arg value ${image_coordinate} '.APP_IMAGE_COORDINATE = $value' ${app_dir}/.build/metadata.json)" > ${app_dir}/.build/metadata.json
}

#####################################
## 构建镜像函数
#####################################
BuildImage(){
  Info "start build app image !!!"
  # 进入Dockerfile文件所在的同级目录
  cd "${app_dir}/.build" || exit 1
  # 构建docker镜像
  sudo docker build \
   --file "${app_dir}/.build/Dockerfile" \
   --build-arg build_version="${app_build_version}" \
   --build-arg timezone="Asia/Shanghai" \
   --tag "${image_coordinate}" .
  # 回到父级目录
  cd "${app_dir}" || exit 1
  # 上传docker镜像
  sudo docker push "${image_coordinate}"
  Info "push image success ,coordinate = '${image_coordinate}'"
  # 删除本地产物镜像
  sudo docker rmi "$(sudo docker images | grep "${app_build_version}" | grep "${image_domain}/${image_library}/${app_name}" | awk '{print $3}')"
  Info "build app images finished and success !!!"
}

#####################################
## 构建K8S部署 函数
#####################################
BuildBlueprint(){
  Info "start build blueprint !!!"
  # 替换镜像坐标
  sed -i "s@image_coordinate:tag@${image_coordinate}@g" "${app_dir}/.build/blueprint.yaml"
}

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
  archive_name="deploy-${app_name}-${app_build_version}.tar.gz"
  # 生成部署压缩包
  tar czf "${archive_name}" blueprint.yaml metadata.json
  # 将部署包拷贝到"产物目录"
  cp -f "${app_dir}/.build/${archive_name}" "${product_dir}"
  # 在"产物目录"下生成deploy.json
  local json_info="{\"DEPLOY_PACKAGE_NAME\":\"${archive_name}\"}"
  echo "${json_info}" >> "${product_dir}/deploy.json"
  # 切换到回原有的目录下
  cd "${current_dir}" || exit 1
  Info "the project has been built completed and success !!!"
}

#####################################
## Clean 函数
#####################################
Clean(){
  # 删除构建文件夹
  local dir="${app_dir}/.build"
  if [ -d "${dir}" ]; then
      rm -rf "${dir}"
  fi
  Info "clean build package product"
}

#####################################
## trap signal 函数
#####################################
TrapSignal(){
  # 捕捉信号，清理任务;脚本解锁
  trap 'Clean;Unlock;exit 0;' EXIT SIGINT
  # 捕捉错误发生位置
  trap 'TraceError $LINENO $BASH_COMMAND' ERR
}

#####################################
## Run 函数
#####################################
Run(){
  Lock
  TrapSignal
  CheckJava
  CheckMaven
  MavenPackage
  MoveFile
  WriteBuildVersion
  BuildInfo
  WriteMetadata
  BuildImage
  BuildBlueprint
  BuildDeployPackage
}
Run