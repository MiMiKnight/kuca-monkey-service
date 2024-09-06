#!/bin/sh
set -ex
# 脚本当前所在目录
current_dir=$(cd "$(dirname "$0")" && pwd)
# 当前脚本所在目录的上一级目录
parent_dir=$(dirname "$current_dir")
# 项目名称
app_name=$(awk -F '=' 'NR==2{print $2}' "${parent_dir}/.build/deployment/metadata.txt")
# 项目版本
app_version=$(awk -F '=' 'NR==4{print $2}' "${parent_dir}/.build/deployment/metadata.txt")
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
# dos2unix
sudo dos2unix "${parent_dir}/.build/deployment/*"
# 构建docker镜像
sudo docker build --file "${parent_dir}/.build/deployment/Dockerfile" \
 --build-arg "${parent_dir}/.build" \
 --tag "${image_name}" .
# 登陆docker
sudo docker login ${image_domain} --username ${image_user} --password ${image_password}
# 上传docker镜像
sudo docker push "${image_name}"
# 退出登陆docker
sudo docker logout
# 清除构建内容
sudo rm -rf "${parent_dir}/.build"