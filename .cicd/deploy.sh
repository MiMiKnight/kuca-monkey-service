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
      Error "Argument \'REPOSITORY_NAME\' must not be empty !!!"
  fi
  if [ -z "${code_repository}" ]; then
      Error "Argument \'CODE_REPOSITORY\' must not be empty !!!"
  fi
  if [ -z "${code_branch}" ]; then
      Error "Argument \'CODE_BRANCH\' must not be empty !!!"
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
     Error "code clone failed !!!"
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
  #sshpass -p "vagrant" scp -c "${random_dir}/${deploy_archive_name}" root@redis.dev.vm.mimiknight.cn:/home/root/${random_dir}/${deploy_archive_name}
  #cp -f ${deploy_json_location} ${script_current_dir}
  #cp -f ${deploy_package_location} ${script_current_dir}
  Info "The upload package finished and success !!!"
}

#####################################
## Deploy 函数
#####################################
Deploy(){
  Info "Start run deploy task !!!"
  # 生成随机目录名称
  local random_dir="";
  random_dir="${script_current_dir}/$(pwgen -ABns0 16 1 | tr a-z A-Z)"
  mkdir -p "${random_dir}"
  # 从git仓库拉取代码
  local project_dir="${random_dir}/${repository_name}";
  GitClone "${project_dir}"
  if [ $? -ne 0 ];then
    # 删除生成的随机目录
    rm -rf "${random_dir}"
    exit 1
  fi

  # dos2unix chmod
  find "${project_dir}/.cicd" -type f -print0 | xargs -0 dos2unix -k -s
  chmod +x "${project_dir}/.cicd/build.sh"

  # 执行构建打包
  /bin/bash "${project_dir}/.cicd/build.sh" "${image_domain}" "${image_user}" "${image_password}" "${image_library}"
  # 如果上一步构建失败则执行if
  if [ $? -ne 0 ];then
    Warn "The build package failed !!!"
    # 删除生成的随机目录
    rm -rf "${random_dir}"
    exit 1
  fi

  local deploy_json_location="" deploy_package_name="" deploy_package_location=""
  # deploy.json文件路径
  deploy_json_location="${random_dir}/${deploy_json_file_name}"
  # 读取部署包名
  deploy_package_name="$(jq -r '.DEPLOY_PACKAGE_NAME' "${deploy_json_location}")"
  # 部署包路径
  deploy_package_location="${random_dir}/${deploy_package_name}"
  # 传包部署
  UploadPackage "${deploy_json_location}" "${deploy_package_location}"
  # 删除随机目录
  rm -rf "${random_dir}"
  #
  Info "The deploy task run finished and success !!!"
}
Deploy
