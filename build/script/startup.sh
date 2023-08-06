#!/bin/sh

# 指定用户

# 运行脚本

chmod +x startup.sh

java -jar ../lib/kuca-monkey-service-0.0.1-SNAPSHOT.jar \
-server \
-Duser.language=en \
-Dfile.encoding=UTF-8 \
-Duser.timezone=GMT+04:00 \
--jasypt.encryptor.password=5177251cc96740fdae33893599768b9e