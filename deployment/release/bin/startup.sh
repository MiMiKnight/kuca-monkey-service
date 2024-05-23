#!/bin/sh
# TODO 脚本待完善
# 指定用户

# 当前shell脚本所在绝对路径目录
current_dir=$(cd $(dirname ${BASH_SOURCE[0]}); pwd );
echo $current_dir
# 当前shell脚本所在绝对路径目录上一级目录
current_dir_parent=`dirname "$current_dir"`;

java -jar $current_dir_parent/lib/@app.jar.name@.jar \
-server \
-Duser.language=en \
-Dfile.encoding=UTF-8 \
-Duser.timezone=GMT+04:00 \
--jasypt.encryptor.password=5177251cc96740fdae33893599768b9e
