# 指定基础镜像
FROM harbor.devops.vm.mimiknight.cn/library/oraclejre:1.8.0-202-ubuntu_v22.04 AS builder
# 维护者
ARG maintainer="victor2015yhm@gmail.com"
# 时区参数
ARG timezone="Asia/Shanghai"
# 用户名
ARG user="service"
# 项目名称
ARG app_name="@app.name@"
# 项目版本
ARG app_version="@app.build.version@"
# 归档文件父目录（必填项）
ARG archive_file_parent_path=''
# 项目归档文件名称
ARG archive_file_name="@app.name@-@app.build.version@".tar.gz
# 设置工作目录
ARG workdir="/opt/app"
# 日志数据卷
ARG volume_log="${workdir}/${app_name}/log"
# 项目端口号
ARG app_port=8443
# 标签
LABEL cn.mimiknight.developer.image.maintainer=${maintainer}
LABEL cn.mimiknight.developer.image.app_name=${app_name}
LABEL cn.mimiknight.developer.image.app_version=${app_version}
#
RUN apt-get install -y tar \
    && echo "${timezone}" > /etc/timezone \
    && useradd --create-home --user-group --home-dir /home/${user} --shell /usr/sbin/nologin --comment "JavaWebService" ${user} \
    && mkdir -p ${workdir}/${app_name} \
    && mkdir -p ${volume_log}
# 屏蔽基础镜像的健康检查
HEALTHCHECK NONE
# 设置工作目录
WORKDIR ${workdir}
# 复制项目归档文件到容器工作目录
COPY ${archive_file_parent_path}/${archive_file_name} ./
#
RUN tar -xf ./${archive_file_name} -C ${workdir}/${app_name} \
    && rm -rf ./${archive_file_name} \
    && chown -R ${user}:${user} ${workdir}/${app_name}
# 定义匿名数据卷
VOLUME ["${volume_log}"]
# 暴露应用程序的端口
EXPOSE ${app_port}
# 设置子级工作目录 ${workdir}/${app_name}
WORKDIR ${app_name}
# 指定后续指令的用户上下文
USER ${user}:${user}
# 运行应用程序
ENTRYPOINT ["sh", "${workdir}/${app_name}/bin/service.sh","start"]