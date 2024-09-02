# 指定基础镜像
# TODO 脚本待完善
FROM openjdk:8-jdk-alpine
MAINTAINER MiMiKnight<victor2015yhm@gmail.com>
# 设置工作目录
WORKDIR /app
# 设置容器时区
RUN echo "Asia/Shanghai" > /etc/timezone
# 复制项目zip到容器中
COPY target/myproject.zip /app
# 解压
# 暴露应用程序的端口
EXPOSE 8443
# 运行应用程序
ENTRYPOINT ["java", "-jar", "myproject.jar"]