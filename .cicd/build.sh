#!/bin/sh
set -ex
# 脚本当前所在目录
current_dir=$(cd "$(dirname "$0")" && pwd)
# 当前脚本所在目录的上一级目录
parent_dir=$(dirname "$current_dir")

# 打包
package(){
  # mvn clean compile package '-Dmaven.test.skip=true'
  echo "package"
}
# 执行项目打包
package

# dos2unix
file_dos2unix(){
 sudo dos2unix "${parent_dir}/.build/blueprint.yaml"
 sudo dos2unix "${parent_dir}/.build/Dockerfile"
 sudo dos2unix "${parent_dir}/.build/metadata.txt"
}

# move deployment
move_deployment(){
  if [ ! -d "${parent_dir}/.build/deployment" ];then
    echo "[Warn] ${parent_dir}/.build/deployment not exist!!!"
    exist 1
  fi
  sudo mv -f ${parent_dir}/.build/deployment/* ${parent_dir}/.build/
  sudo rm -rf "${parent_dir}/.build/deployment"
  file_dos2unix
}
# 移动部署文件
move_deployment

# 项目名称
app_name="$(awk -F '=' 'NR==2{print $2}' "${parent_dir}/.build/metadata.txt")"
# 项目版本
app_version="$(awk -F '=' 'NR==4{print $2}' "${parent_dir}/.build/metadata.txt")"
# 镜像仓库用户名
image_user="mmk"
# 镜像仓库密码
image_password="Harbor12345"
# 镜像仓库域名
image_domain="harbor.devops.vm.mimiknight.cn"
# 镜像仓库名
image_library="mmkd"
# 项目镜像名
image_name="${image_domain}/${image_library}/${app_name}:${app_version}"

# 构建镜像函数
build_image(){
 # 进入Dockerfile文件所在的同级目录
 cd "${parent_dir}/.build"
 # 构建docker镜像
 sudo docker build \
  --file "${parent_dir}/.build/Dockerfile" \
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

# 构建镜像
build_image

# 清除构建内容
sudo rm -rf "${parent_dir}/.build"