# 指定基础镜像
FROM registry.cn-hangzhou.aliyuncs.com/mimiknight/oraclejdk:1.8.0-202-ubuntu_v24.04 AS builder
# 维护者
ARG maintainer="victor2015yhm@gmail.com"
# 时区参数
ARG timezone="Asia/Shanghai"
# 用户名
ARG user="service"
# 项目名称
ARG app_name="@app.name@"
# 项目版本
ARG build_version=""
# 项目归档文件名称
ARG archive_file_name="@app.name@.tar.gz"
# 设置工作目录
ARG workdir="/opt/app"
# 项目端口号
ARG app_port=8443
# 标签
LABEL cn.mimiknight.developer.image.maintainer=${maintainer}
LABEL cn.mimiknight.developer.image.app_name=${app_name}
LABEL cn.mimiknight.developer.image.build_version=${build_version}
# 拷贝
COPY ./${archive_file_name} ./
#
RUN useradd --create-home --user-group --home-dir /home/${user} --shell /usr/sbin/nologin --comment "WebServiceUser" ${user} \
    && echo "${timezone}" > /etc/timezone \
    && mkdir -p "${workdir}/${app_name}" \
    && mkdir -p "${workdir}/${app_name}/logs" \
    && tar -xf "./${archive_file_name}" -C "${workdir}/${app_name}" \
    && rm -rf "./${archive_file_name}" \
    && find "${workdir}/${app_name}" -type f -print0 | xargs -0 dos2unix -k -s \
    && chown -R ${user}:${user} "${workdir}/${app_name}" \
    && chmod 755 "${workdir}/${app_name}"
# 定义匿名数据卷
VOLUME ["${workdir}/${app_name}/logs"]
# 环境变量
ENV APP_HOME=${workdir}/${app_name}
ENV PATH=$APP_HOME/bin:$PATH
# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD /bin/bash ${workdir}/${app_name}/bin/service.sh healthcheck
# 暴露应用程序的端口
EXPOSE ${app_port}
# 设置工作目录
WORKDIR "${workdir}/${app_name}"
# 指定后续指令的用户上下文
USER ${user}:${user}
# 运行应用程序
ENTRYPOINT ["/bin/bash","./bin/service.sh","start"]
