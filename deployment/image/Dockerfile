# 指定基础镜像
FROM openjdk:8-jdk-alpine AS builder
# 时区参数
ARG timezone="Asia/Shanghai"
# 用户名
ARG user="service"
# 设置工作目录
ARG workdir="/opt/app"
# 项目名称
ARG app_name="@app.name@"
# 项目归档文件名称
ARG archive_file_name="@app.name@-@app.build.version@".tar.gz
# 日志数据卷
ARG volume_log="${workdir}/${app_name}/log"
# 项目端口号
ARG app_port=8443
# 标签
LABEL maintainer="victor2015yhm@gmail.com"
#
RUN sudo apt-get install -y wget tar \
    && sudo echo "${timezone}" > /etc/timezone \
    && sudo useradd --create-home --user-group --home-dir /home/${user} --shell /usr/sbin/nologin --comment "JavaWebService" ${user} \
    && sudo mkdir -p ${workdir}/${app_name} \
    && sudo mkdir -p ${volume_log} \
    && sudo chmod -R 750 ${workdir}/${app_name} \
    && sudo chown -R ${user}:${user} ${workdir}/${app_name}
# 设置工作目录
WORKDIR ${workdir}
# 指定后续指令的用户上下文
USER ${user}:${user}
# 屏蔽基础镜像的健康检查
HEALTHCHECK NONE
# 复制项目归档文件到容器工作目录
COPY --chown=${user}:${user} ../${archive_file_name} ./
# 解压
RUN tar -xvf ./${archive_file_name} -C ${workdir}/${app_name} \
    && rm -rf ./${archive_file_name}
# 定义匿名数据卷
VOLUME ["${volume_log}"]
# 暴露应用程序的端口
EXPOSE ${app_port}
# 运行应用程序
ENTRYPOINT ["sh", "${workdir}/${app_name}/bin/service.sh","start"]