#!/bin/sh
set -ex
# 脚本当前所在目录
current_dir=$(cd "$(dirname "$0")" && pwd)
# 当前脚本所在目录的上一级目录
parent_dir=$(dirname "$current_dir")

# 获取配置信息
get_metadata(){
  case "$1" in
  'APP_NAME')
    line_number=2
    ;;
  'APP_BUILD_VERSION')
    line_number=4
    ;;
  'ARCHIVE_FILE_NAME')
    line_number=6
    ;;
  'APP_MAIN_CLASS')
    line_number=8
    ;;
  'APP_JAR_NAME')
    line_number=10
    ;;
  *)
    echo "[APP_NAME|APP_BUILD_VERSION|ARCHIVE_FILE_NAME|APP_MAIN_CLASS|APP_JAR_NAME]"
    exit 1
    ;;
  esac
  return "$(awk -F '=' 'NR==${line_number}{print $2}' "${parent_dir}/.build/deployment/metadata.txt")"
}

# 项目名称
app_name=get_metadata "APP_NAME"
# 项目版本
app_version=get_metadata "APP_BUILD_VERSION"
# 项目归档文件名
app_archive_file_name=get_metadata "ARCHIVE_FILE_NAME"
# 镜像仓库用户名
image_user="mmk"
# 镜像仓库密码
image_password="Harbor12345"
# 镜像仓库域名
image_domain="harbor.devops.vm.mimiknight.cn"
# 镜像仓库名
image_library="mmkd"
# 项目镜像名
image_name=${image_domain}/${image_library}/${app_name}:${app_version}

# # dos2unix
file_dos2unix(){
 sudo dos2unix "${parent_dir}/.build/deployment/blueprint.yaml"
 sudo dos2unix "${parent_dir}/.build/deployment/Dockerfile"
 sudo dos2unix "${parent_dir}/.build/deployment/metadata.txt"
}

# 构建镜像函数
build_image(){
 mv "${parent_dir}/.build/${app_archive_file_name}" "${parent_dir}/.build/deployment/${app_archive_file_name}"
 # 进入Dockerfile文件所在的同级目录
 cd "${parent_dir}/.build/deployment"
 # 构建docker镜像
 sudo docker build --file "${parent_dir}/.build/deployment/Dockerfile" \
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

# 文件dos2unix
file_dos2unix
# 构建镜像
build_image

# 清除构建内容
sudo rm -rf "${parent_dir}/.build"