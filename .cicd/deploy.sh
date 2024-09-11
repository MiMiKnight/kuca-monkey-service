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
# 程序锁文件路径
lock_file_location=""
# 代码仓库名称
code_repository_name=$1
# 代码仓库地址
code_repository_url=$2
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
# 项目构建脚本相对路径（相对项目所在的位置）
build_script_relative_dir=".cicd/build.sh"


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
## 只针对同一代码仓库同一代码分支项目进行脚本在执行时加锁
#####################################
Lock(){
  local lock_file_name="${code_repository_name}-${code_branch}-lj65p2dm7sxos9hqx6gw.lock"
  lock_file_location="${script_current_dir}/${lock_file_name}"
  if [ -f "${lock_file_location}" ]; then
     # 读取任务ID
     local task_id=""
     task_id="$(cat "${lock_file_location}")"
     Warn "there are already running deploy task ,task_id = '${task_id}'. please try again later !!!"
     Warn "program lock = '${lock_file_name}'"
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
     # 修改文件可被删除编辑修改
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
## Check Arg 函数
#####################################
CheckArg(){
  if [ -z "${code_repository_name}" ]; then
      Error "Arg: 'code_repository_name' value invalid !!!"
  fi
  if [ -z "${code_repository_url}" ]; then
      Error "Arg: 'code_repository_url' value invalid !!!"
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
  local mvn_location="${MAVEN_HOME}/bin/mvn"
  if [ ! -e "${mvn_location}" ] || [ ! -x "${mvn_location}" ]; then
     Error "Please install Maven and set environment variables or check it !!!"
  fi
}

#####################################
## Git Clone 函数
## $1 克隆至目的文件夹（必填）
#####################################
GitClone(){
  Info "start clone code !!!"
  Info "repository: ${code_repository_url}  branch: ${code_branch}"
  # 目的文件夹
  local dest=$1
  git clone "${code_repository_url}" --branch "${code_branch}" "${dest}"
  if [ $? -ne 0 ];then
     Error "code clone failed !!!"
  fi
  Info "the code clone finished and success !!!"
}

#####################################
## 传包 函数
## $1 deploy.json 文件路径
## $2 deploy-xxx.tar.gz 包文件路径
#####################################
UploadPackage(){
  Info "start upload deploy package !!!"
  local deploy_json_location="$1" deploy_package_location="$2";
  #sshpass -p "vagrant" scp -c "${build_dir}/${deploy_archive_name}" root@redis.dev.vm.mimiknight.cn:/home/root/${build_dir}/${deploy_archive_name}
  #cp -f ${deploy_json_location} ${script_current_dir}
  #cp -f ${deploy_package_location} ${script_current_dir}
  Info "the upload deploy package finished and success !!!"
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
  # 登陆docker
  sudo docker login "${image_domain}" --username "${image_user}" --password "${image_password}"
  Info "login docker success !!!"
}

#####################################
## 删除目录 函数
#####################################
DeleteBuildDir(){
  local dir=${temp_build_dir}
  if [ -n "${dir}" ] && [ -d "${dir}" ]; then
      rm -rf "${dir}"
      Info "delete build dir success ,dir=${dir} !!!"
  fi
}

#####################################
## 写入任务ID 函数
#####################################
WriteTaskID(){
  local task_id=$1
  if [ -f "${lock_file_location}" ]; then
     chattr -i "${lock_file_location}"
     chmod 640 "${lock_file_location}"
     # 写入任务ID
     echo "${task_id}" > "${lock_file_location}"
     # 修改文件只读
     chmod 440 "${lock_file_location}"
     # 修改文件不可被删除编辑修改
     chattr +i "${lock_file_location}"
  fi
}

#####################################
## 生成临时构建目录 函数
#####################################
CreateBuildDir(){
  local random="" dir=""
  random=$(pwgen -ABns0 16 1 | tr a-z A-Z)
  WriteTaskID "${random}"
  dir="${script_current_dir}/${random}"
  mkdir -p "${dir}"
  temp_build_dir="${dir}"
  Info "create build dir success,dir=${dir} !!!"
}

#####################################
## Deploy 函数
#####################################
Deploy(){
  Info "start run deploy task !!!"
  # 创建构建目录
  CreateBuildDir
  local build_dir="";
  build_dir=${temp_build_dir}

  # 从git仓库拉取代码
  local project_dir="${build_dir}/${code_repository_name}";
  GitClone "${project_dir}"

  # dos2unix chmod
  local build_script_location="${project_dir}/${build_script_relative_dir}"
  if [ ! -f "${build_script_location}" ]; then
     Error "project build script file is missing !!!"
  fi
  dos2unix -k -s "${build_script_location}"
  chmod +x "${project_dir}/${build_script_relative_dir}"

  # 登录docker
  LoginDocker
  # 执行构建打包
  /bin/bash "${build_script_location}" "${image_domain}" "${image_library}" "${build_dir}"

  local deploy_json_location="" deploy_package_name="" deploy_package_location=""
  # deploy.json文件路径
  deploy_json_location="${build_dir}/${deploy_json_file_name}"
  # 读取部署包名
  deploy_package_name="$(jq -r '.DEPLOY_PACKAGE_NAME' "${deploy_json_location}")"
  # 部署包路径
  deploy_package_location="${build_dir}/${deploy_package_name}"
  # 传包部署
  UploadPackage "${deploy_json_location}" "${deploy_package_location}"
  #
  Info "the deploy task run finished and success !!!"
}

#####################################
## trap signal 函数
#####################################
TrapSignal(){
  # 捕捉信号，删除临时构建目录,退出docker登陆;脚本解锁
  trap 'DeleteBuildDir;LogoutDocker;Unlock;exit 0;' EXIT SIGINT
  # 捕捉错误发生位置
  trap 'TraceError $LINENO $BASH_COMMAND' ERR
}

#####################################
## Run 函数
#####################################
Run(){
  Lock
  TrapSignal
  CheckArg
  CheckJava
  CheckMaven
  Deploy
}
Run


