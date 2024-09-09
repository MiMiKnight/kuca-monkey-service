#!/bin/bash
#set -ex

############全局变量常量############
# 脚本当前所在目录 常量
CONST_CURRENT_DIR=$(cd "$(dirname "$0")" && pwd)
declare -r CONST_CURRENT_DIR;
# 脚本所在的上一级目录 常量
CONST_PARENT_DIR=$(dirname "$CONST_CURRENT_DIR")
declare -r CONST_PARENT_DIR;
# 项目jar包路径 常量
CONST_APP_JAR_LOCATION="${CONST_PARENT_DIR}/lib/@app.jar.name@.jar"
declare -r CONST_APP_JAR_LOCATION;
# 启动日志位置 常量
CONST_APP_STARTUP_LOG_LOCATION="${CONST_PARENT_DIR}/logs/startup.log"
declare -r CONST_APP_STARTUP_LOG_LOCATION;
# shell内的JAVA_HOME环境变量
declare -x EVN_JAVA_HOME="/opt/app/java"
# JAVA_OPTS 常量
CONST_JAVA_OPTS="-Xms512m \
  -Xmx1024m \
  -XX:MetaspaceSize=512m \
  -XX:MaxMetaspaceSize=1024m \
  -server \
  -Duser.language=en \
  -Duser.timezone=GMT+00:00 \
  -Dfile.encoding=utf-8 \
  -Dspring.profiles.name=application \
  -Dspring.profiles.active=debug"
declare -r CONST_JAVA_OPTS;

##################################
# 友好提示函数
##################################
Info() {
  echo -e "\e[1;32;49m[INFO] \e[1;39;49m$1\e[0m";
}

##################################
# 警告提示函数
##################################
Warn() {
  echo -e "\e[1;33;49m[WARN] \e[1;39;49m$1\e[0m";
}

##################################
# 错误提示退出函数
##################################
Error() {
  echo -e "\e[1;31;49m[ERROR] \e[1;39;49m$1\e[0m";
}

##################################
# 设置JAVA环境变量函数
# $1 参数1：JAVA安装目录（非必填参数）
##################################
GetJavaHome() {
  # java安装位置
  local java_install_path=$1;

  [ -e "${java_install_path}/bin/java" ] && JAVA_HOME=${java_install_path}
  [ ! -e "${JAVA_HOME}/bin/java" ] && JAVA_HOME=$HOME/jdk/java
  [ ! -e "${JAVA_HOME}/bin/java" ] && JAVA_HOME=/usr/java
  [ ! -e "${JAVA_HOME}/bin/java" ] && unset JAVA_HOME

  # JAVA_HOME 目录存在
  if [ -d "${JAVA_HOME}" ]; then
     EVN_JAVA_HOME=${JAVA_HOME}
     return
  else
     JAVA_PATH=$(which java)
     # 判断java执行文件是否存在
     if [ -z "$JAVA_PATH" ]; then
       Error "Please install Java and set environment variables, We need java(x64) and jdk8 or later is better !!!"
       exit 1
     else
       JAVA_HOME=$(dirname "$JAVA_PATH")
       JAVA_HOME=$(cd "$(dirname "$JAVA_HOME")" && pwd)
       EVN_JAVA_HOME=${JAVA_HOME}
       return
     fi
  fi
}

##################################
# start函数
##################################
Start() {
  GetJavaHome ''
  # 启动应用（不可后台启动，必须前台启动）
  nohup ${EVN_JAVA_HOME}/bin/java ${CONST_JAVA_OPTS} -jar ${CONST_APP_JAR_LOCATION} > ${CONST_APP_STARTUP_LOG_LOCATION} 2>&1
}

##################################
# 健康检查函数
##################################
HealthCheck(){
  exit 0
}

##################################
# usage函数
##################################
usage() {
  case "$1" in
  'start')
    Start
    ;;
  'healthcheck')
    HealthCheck
    ;;
  *)
    echo "args [start|healthcheck]"
    ;;
  esac
}

# 调用usage函数
usage "$1"