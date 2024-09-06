# 项目名：kuca-monkey-service

## Environment

- JDK OracleJDK v17.0.8
- maven 3.9.0-3.9.4
- IntelliJIDEA ideaIU-2022.3.3.win
- MySQL v8.0.34
- Redis v3.0.504
- spring-boot v2.7.12
- mybatis-spring-boot-starter v2.3.1
- mysql-connector-java v8.0.33

## Debug VM Option

> - -server
> - -Duser.language=en
> - -Dfile.encoding=UTF-8
> - -Duser.timezone=GMT+00:00
> - -Dspring.profiles.name=application
> - -Dspring.profiles.active=debug,custom
> - -Dspring.config.additional-location="F:/Workspace/CodeWorkspace/JavaProject/IncubationProject/kuca-monkey-service/deployment/debug/config/"

## Not Debug VM Option

> - -server
> - -Duser.language=en
> - -Dfile.encoding=UTF-8
> - -Duser.timezone=GMT+00:00
> - -Djasypt.encryptor.password=5177251cc96740fdae33893599768b9e

## jasypt加解密

> - [jasypt GitHub 仓库](https://github.com/ulisesbocchio/jasypt-spring-boot)

```xml
<plugin>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-maven-plugin</artifactId>
</plugin>
```

> - 加密
> - mvn jasypt:encrypt-value -Djasypt.encryptor.password="the password" -Djasypt.plugin.value="theValueYouWantToEncrypt"
> - 加密案例：（使用5177251cc96740fdae33893599768b9e加密123456）
> - mvn jasypt:encrypt-value -Djasypt.encryptor.password="5177251cc96740fdae33893599768b9e" -Djasypt.plugin.value="123456"
> - 解密
> - mvn jasypt:decrypt-value -Djasypt.encryptor.password="the password" -Djasypt.plugin.value="theValueYouWantToDecrypt"
> - 解密案例：
> - mvn jasypt:decrypt-value -Djasypt.encryptor.password="5177251cc96740fdae33893599768b9e" -Djasypt.plugin.value="theValueYouWantToDecrypt"

## 项目打包

> - mvn clean package assembly:single '-Dmaven.test.skip=true'
> - 或者
> - mvn clean package '-Dmaven.test.skip=true'
> - chmod +x .cicd/build.sh
> - dos2unix .cicd/build.sh
