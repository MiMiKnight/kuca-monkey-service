#!/bin/bash
set -ex
#################################
## deploy.sh
## 描述：部署脚本
#################################
# sudo apt-get install pwgen
# sudo apt-get install sshpass

############全局变量常量############
# 脚本当前所在目录 常量
C_SCRIPT_CURRENT_DIR=$(cd "$(dirname "$0")" && pwd)
declare -r C_SCRIPT_CURRENT_DIR;
# 脚本所在的上一级目录 常量
C_SCRIPT_PARENT_DIR=$(dirname "$C_SCRIPT_CURRENT_DIR")
declare -r C_SCRIPT_PARENT_DIR;
# 部署包名称
declare G_DEPLOY_ARCHIVE_NAME="deploy-*.tar.gz"
# 代码仓库名称
C_REPOSITORY_NAME=$1
declare -r C_REPOSITORY_NAME;
# 代码仓库地址
C_CODE_REPOSITORY=$2
declare -r C_CODE_REPOSITORY;
# 代码分支
C_CODE_BRANCH=$3
declare -r C_CODE_BRANCH;
# 用户Git密码
C_USER_GIT_PASSWORD=$4
declare -r C_USER_GIT_PASSWORD;
# deploy.json 文件名
C_DEPLOY_JSON_FILE_NAME="deploy.json"
declare -r C_DEPLOY_JSON_FILE_NAME;

#####################################
## Check Arg 函数
#####################################
CheckArg(){
  if [ -z "${C_REPOSITORY_NAME}" ]; then
      echo "[Warn] repository name must not be empty !!!"
      exit 0
  fi
  if [ -z "${C_CODE_REPOSITORY}" ]; then
      echo "[Warn] code repository url must not be empty !!!"
      exit 0
  fi
  if [ -z "${C_CODE_BRANCH}" ]; then
      echo "[Warn] code branch must not be empty !!!"
      exit 0
  fi
}
CheckArg

#####################################
## Check Java 函数
#####################################
CheckJava(){
  local java_location="${JAVA_HOME}/bin/java"
  if [ ! -e "${java_location}" ] || [ ! -x "${java_location}" ]; then
     echo "[Warn] Please install Java and set environment variables or check it !!!"
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
     echo "[Warn] Please install Maven and set environment variables or check it !!!"
     exit 1
  fi
}
CheckMaven

#####################################
## Git Clone 函数
## $1 克隆至目的文件夹（必填）
#####################################
GitClone(){
  # 目的文件夹
  local dest=$1
  #git clone "${C_CODE_REPOSITORY}" --branch "${C_CODE_BRANCH}" "${dest}"
sudo /usr/bin/expect << EOF
set timeout 60
spawn git clone "${C_CODE_REPOSITORY}" --branch "${C_CODE_BRANCH}" "${dest}"
expect "Enter passphrase for key" { send "HNNUjsjx1\r" }
expect eof
EOF
  if [ $? -ne 0 ];then
     echo "[Warn] clone failed !!!"
     exit 1
  fi
}

#####################################
## 传包 函数
## $1 deploy.json 文件路径
## $2 deploy-xxx.tar.gz 包文件路径
#####################################
UploadPackage(){
 local deploy_json_location="$1" deploy_package_location="$2";
 #sshpass -p "vagrant" scp -c "${random_dir}/${deploy_archive_name}" root@redis.dev.vm.mimiknight.cn:/home/root/${random_dir}/${deploy_archive_name}
 cp -f ${deploy_json_location} ${C_SCRIPT_CURRENT_DIR}
 cp -f ${deploy_package_location} ${C_SCRIPT_CURRENT_DIR}
 echo "UploadPackage"
}

#####################################
## Deploy 函数
#####################################
Deploy(){
  # 生成随机目录名称
  local random_dir="";
  random_dir="${C_SCRIPT_CURRENT_DIR}/$(pwgen -ABns0 16 1 | tr a-z A-Z)"
  mkdir -p "${random_dir}"
  # 从git仓库拉取代码
  local project_dir="${random_dir}/${C_REPOSITORY_NAME}";
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
  /bin/bash "${project_dir}/.cicd/build.sh"
  # 如果上一步构建失败则执行if
  if [ $? -ne 0 ];then
    # 删除生成的随机目录
    rm -rf "${random_dir}"
    exit 1
  fi

  local deploy_json_location="" deploy_package_name="" deploy_package_location=""
  # deploy.json文件路径
  deploy_json_location="${random_dir}/${C_DEPLOY_JSON_FILE_NAME}"
  # 读取部署包名
  deploy_package_name="$(jq -r '.DEPLOY_PACKAGE_NAME' "${deploy_json_location}")"
  # 部署包路径
  deploy_package_location="${random_dir}/${deploy_package_name}"
  # 传包部署
  UploadPackage "${deploy_json_location}" "${deploy_package_location}"
  # 删除随机目录
  rm -rf "${random_dir}"
}
Deploy

#
echo "Run deploy task finished !!!"