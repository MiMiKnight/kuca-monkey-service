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
# 项目归档文件名称
ARG archive_file_name="@app.name@-@app.build.version@".tar.gz
# 设置工作目录
ARG workdir="/opt/app"
# 日志数据卷
ARG volume_log="${workdir}/${app_name}/log"
# 项目端口号
ARG app_port=8443
# APT镜像
ARG apt_mirror="https://mirrors.tuna.tsinghua.edu.cn"
# 标签
LABEL cn.mimiknight.developer.image.maintainer=${maintainer}
LABEL cn.mimiknight.developer.image.app_name=${app_name}
LABEL cn.mimiknight.developer.image.app_version=${app_version}
#
RUN cp -a /etc/apt/sources.list /etc/apt/sources.list.bak \
    && sed -i "s@http://.*archive.ubuntu.com@${apt_mirror}@g" /etc/apt/sources.list \
    && sed -i "s@http://.*security.ubuntu.com@${apt_mirror}@g" /etc/apt/sources.list \
    && sed -i "s@https://@http://@g" /etc/apt/sources.list \
    && apt-get update \
    && apt-get install -y --reinstall ca-certificates \
    && sed -i "s@http://@https://@g" /etc/apt/sources.list \
    && apt-get install -y dos2unix tar vim \
    && mv -f /etc/apt/sources.list.bak /etc/apt/sources.list \
    && echo "${timezone}" > /etc/timezone \
    && useradd --create-home --user-group --home-dir /home/${user} --shell /usr/sbin/nologin --comment "JavaWebService" ${user} \
    && mkdir -p ${workdir}/${app_name} \
    && mkdir -p ${volume_log}
# 设置工作目录
WORKDIR ${workdir}
# 复制项目归档文件到容器工作目录
COPY ./${archive_file_name} ./
#
RUN tar -xf ./${archive_file_name} -C ${workdir}/${app_name} \
    && rm -rf ./${archive_file_name} \
    && find "${workdir}/${app_name}" -type f -print0 | xargs -0 dos2unix -k -s \
    && chown -R ${user}:${user} ${workdir}/${app_name}
# 环境变量
ENV APP_HOME=${workdir}/${app_name}
ENV PATH=$APP_HOME/bin:$PATH
# 指定后续指令的用户上下文
USER ${user}:${user}
# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD /bin/bash ${workdir}/${app_name}/bin/app_service.sh healthcheck
# 定义匿名数据卷
VOLUME ["${volume_log}"]
# 暴露应用程序的端口
EXPOSE ${app_port}
# 设置子级工作目录 ${workdir}/${app_name}
WORKDIR ${app_name}
# 运行应用程序
ENTRYPOINT ["/bin/bash","./bin/app_service.sh","start"]
