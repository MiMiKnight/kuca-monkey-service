#!/bin/bash
set -eu
# set -x 参数 作用：显示参数值（调试脚本时打开，平时注释）
#################################
## deploy.sh
## 描述：部署脚本
## $1: 代码仓库名称
## $2: 代码仓库地址
## $3: 代码分支名称
## $4: 镜像仓库域名
## $5: 镜像仓库用户名
## $6: 镜像仓库用户密码
## $7: 镜像仓库指定库名
#################################
# sudo apt-get install pwgen
# sudo apt-get install sshpass

##############全局变量##############
# 脚本当前所在目录
script_current_dir=$(cd "$(dirname "$0")" && pwd)
# 代码仓库名称
repository_name=$1
# 代码仓库地址
code_repository=$2
# 代码分支
code_branch=$3
# 镜像仓库域名
image_domain=$4
# 镜像仓库用户名
image_user=$5
# 镜像仓库密码
image_password=$6
# 镜像仓库名
image_library=$7
# deploy.json 文件名
deploy_json_file_name="deploy.json"
# 临时构建目录
temp_build_dir=""


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
## Check Arg 函数
#####################################
CheckArg(){
  if [ -z "${repository_name}" ]; then
      Error "Arg: 'repository_name' value invalid !!!"
  fi
  if [ -z "${code_repository}" ]; then
      Error "Arg: 'code_repository' value invalid !!!"
  fi
  if [ -z "${code_branch}" ]; then
      Error "Arg: 'code_branch' value invalid !!!"
  fi
  if [ -z "${image_domain}" ]; then
      Error "Arg: 'image_domain' value invalid !!!"
  fi
  if [ -z "${image_user}" ]; then
      Error "Arg: 'image_user' value invalid !!!"
  fi
  if [ -z "${image_password}" ]; then
      Error "Arg: 'image_password' value invalid !!!"
  fi
  if [ -z "${image_library}" ]; then
      Error "Arg: 'image_library' value invalid !!!"
  fi
}
CheckArg

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
CheckMaven

#####################################
## Git Clone 函数
## $1 克隆至目的文件夹（必填）
#####################################
GitClone(){
  Info "Start clone code !!!"
  Info "repository: ${code_repository}  branch: ${code_branch}"
  # 目的文件夹
  local dest=$1
  git clone "${code_repository}" --branch "${code_branch}" "${dest}"
  if [ $? -ne 0 ];then
     Error "Code clone failed !!!"
  fi
  Info "The code clone finished and success !!!"
}

#####################################
## 传包 函数
## $1 deploy.json 文件路径
## $2 deploy-xxx.tar.gz 包文件路径
#####################################
UploadPackage(){
  Info "Start upload package !!!"
  local deploy_json_location="$1" deploy_package_location="$2";
  #sshpass -p "vagrant" scp -c "${build_dir}/${deploy_archive_name}" root@redis.dev.vm.mimiknight.cn:/home/root/${build_dir}/${deploy_archive_name}
  #cp -f ${deploy_json_location} ${script_current_dir}
  #cp -f ${deploy_package_location} ${script_current_dir}
  Info "The upload package finished and success !!!"
}

#####################################
## logout docker 函数
#####################################
LogoutDocker(){
  # 退出登陆docker
  sudo docker logout
  Info "logout docker success !!!"
}

#####################################
## login docker 函数
#####################################
LoginDocker(){
  # 捕捉脚本退出信号，登出docker
  trap '$(LogoutDocker)' exit
  # 登陆docker
  sudo docker login "${image_domain}" --username "${image_user}" --password "${image_password}"
  Info "login docker success !!!"
}

#####################################
## 删除临时构建目录 函数
#####################################
DeleteBuildDir(){
  if [ -n "${temp_build_dir}" ] && [ -d "${temp_build_dir}" ]; then
      rm -rf "${temp_build_dir}"
      Info "delete temp build dir success ,dir=${temp_build_dir} !!!"
  fi
}

#####################################
## 生成临时构建目录 函数
#####################################
CreateBuildDir(){
  local dir="${script_current_dir}/$(pwgen -ABns0 16 1 | tr a-z A-Z)"
  mkdir -p "${dir}"
  temp_build_dir="${dir}"
  # 捕捉脚本退出信号，删除临时构建目录
  trap '$(DeleteBuildDir)' exit
}

#####################################
## Deploy 函数
#####################################
Deploy(){
  Info "Start run deploy task !!!"
  # 生成随机目录名称
  local build_dir="";
  CreateBuildDir
  build_dir=${temp_build_dir}
  Info "Create build dir success,dir=${build_dir} !!!"

  # 从git仓库拉取代码
  local project_dir="${build_dir}/${repository_name}";
  GitClone "${project_dir}"

  # dos2unix chmod
  find "${project_dir}/.cicd" -type f -print0 | xargs -0 dos2unix -k -s
  chmod +x "${project_dir}/.cicd/build.sh"

  # 登录docker
  LoginDocker
  # 执行构建打包
  /bin/bash "${project_dir}/.cicd/build.sh" "${image_domain}" "${image_library}"

  local deploy_json_location="" deploy_package_name="" deploy_package_location=""
  # deploy.json文件路径
  deploy_json_location="${build_dir}/${deploy_json_file_name}"
  # 读取部署包名
  deploy_package_name="$(jq -r '.DEPLOY_PACKAGE_NAME' "${deploy_json_location}")"
  # 部署包路径
  deploy_package_location="${build_dir}/${deploy_package_name}"
  # 传包部署
  UploadPackage "${deploy_json_location}" "${deploy_package_location}"
  # 删除随机目录
  #rm -rf "${build_dir}"
  #
  Info "The deploy task run finished and success !!!"
}
Deploy
