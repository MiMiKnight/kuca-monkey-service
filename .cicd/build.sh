#!/bin/sh
set -ex

# 项目名称
app_name=$(awk 'NR==2{print $3}' ../.build/deployment/metadata.txt | cut -d = -f -1)
# 项目版本
app_version=$(awk 'NR==4{print $3}' ../.build/deployment/metadata.txt | cut -d = -f -1)
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
# 构建docker镜像
sudo docker build --file ../.build/deployment/Dockerfile --tag ${image_name} .
# 登陆docker
sudo docker login ${image_domain} --username ${image_user} --password ${image_password}
# 上传docker镜像
sudo docker push ${image_name}
# 退出登陆docker
sudo docker logout
# 清除构建内容
sudo rm -rf ../.build