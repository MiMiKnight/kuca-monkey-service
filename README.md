# 项目名：kuca-monkey-service

## VM Option
> - -server
> - -Duser.language=en
> - -Dfile.encoding=UTF-8
> - -Duser.timezone=GMT+04:00
> - -Djasypt.encryptor.password=5177251cc96740fdae33893599768b9e

## 项目打包
> - 不要使用idea自带的插件右键'assembly:single'打包，否则会缺失生成本项目自身的jar包且报如下的错误；
> - 错误提示：it doesn't have an associated file or directory.
> - 使用以下的命令手动打包，可正确打包
> - mvn clean package assembly:single '-Dmaven.test.skip=true'

## 运行项目
```shell
cd ./KucaMonkeyService/lib

java -jar kuca-monkey-service-0.0.1-SNAPSHOT.jar \
-server \
-Duser.language=en \
-Dfile.encoding=UTF-8 \
-Duser.timezone=GMT+04:00 \
--jasypt.encryptor.password=5177251cc96740fdae33893599768b9e
```