#!/bin/bash
#set -ex

############全局变量常量############
# 脚本当前所在目录 常量
CONST_CURRENT_DIR=$(cd "$(dirname "$0")" && pwd)
declare -r CONST_CURRENT_DIR;
# 脚本所在的上一级目录 常量
CONST_PARENT_DIR=$(dirname "$CONST_CURRENT_DIR")
declare -r CONST_PARENT_DIR;
# 项目名称 常量
CONST_APP_NAME="@app.name@"
declare -r CONST_APP_NAME;
# 项目jar包路径 常量
CONST_APP_JAR_LOCATION="${CONST_PARENT_DIR}/lib/@app.jar.name@.jar"
declare -r CONST_APP_JAR_LOCATION;
# 启动日志位置 常量
CONST_APP_STARTUP_LOG_LOCATION="${CONST_PARENT_DIR}/logs/startup.log"
declare -r CONST_APP_STARTUP_LOG_LOCATION;
# shell内的JAVA_HOME环境变量
declare -x EVN_JAVA_HOME="/opt/app/java"
# app_pid 全局变量
declare -i g_app_pid=0
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
Tip() {
  echo "[TIP]: $1"
}

##################################
# 警告提示函数
##################################
Warn() {
  echo "[WARN]: $1"
}

##################################
# 错误提示退出函数
##################################
Error() {
  echo "[ERROR]: $1"
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
       error "Please install Java and set environment variables, We need java(x64) and jdk8 or later is better!"
     else
       JAVA_HOME=$(dirname "$JAVA_PATH")
       JAVA_HOME=$(cd "$(dirname "$JAVA_HOME")" && pwd)
       EVN_JAVA_HOME=${JAVA_HOME}
       return
     fi
  fi
}
#
GetJavaHome ''

##################################
# 获取应用PID 函数
# $1 参数1：进程名称（必填参数）
##################################
GetJavaAppPID() {
  local process_Name="${CONST_APP_JAR_LOCATION}" jps_info='';
  jps_info=$("${EVN_JAVA_HOME}/bin/jps" -l | grep "${process_Name}")
  if [ -n "${jps_info}" ]; then
    g_app_pid=$(echo "${jps_info}" | awk '{print $1}')
  else
    g_app_pid=0
  fi
}

##################################
# start函数
##################################
Start() {
  # 检查应用是否已经启动
  GetJavaAppPID
  if [ ${g_app_pid} -gt 0 ]; then
      Tip "${CONST_APP_NAME} application is already started !!! (pid=${g_app_pid})"
      return
  fi

  local java_cmd='';
  # 启动超时时间，单位：秒
  local -i timeout=60;

  java_cmd="nohup ${EVN_JAVA_HOME}/bin/java ${CONST_JAVA_OPTS} -jar ${CONST_APP_JAR_LOCATION} > ${CONST_APP_STARTUP_LOG_LOCATION} 2>&1 &"
  `${java_cmd}`
  echo "${CONST_APP_NAME} application start successfully!!!"
}

##################################
# stop函数
##################################
Stop() {
  # 检查应用是否已经启动
  GetJavaAppPID
  if [ ${g_app_pid} -gt 0 ]; then
      kill -9 ${g_app_pid}
      echo "${CONST_APP_NAME} application stop successfully!!!"
      return
  else
      echo "${CONST_APP_NAME} application is not running!!!"
  fi
}

##################################
# restart函数
##################################
Restart() {
  stop
  start
  echo "${CONST_APP_NAME} application restart successfully!!!"
}

##################################
# status函数
##################################
Status() {
  GetJavaAppPID
  if [ ${g_app_pid} -gt 0 ]; then
      echo "${CONST_APP_NAME} application is running,pid = ${g_app_pid}"
  else
      echo "${CONST_APP_NAME} application is not running"
  fi
}

##################################
# 健康检查函数
##################################
HealthCheck(){
  exit 0
}

##################################
# info函数
##################################
Info() {
  echo "System information:"
  echo "***********************"
  echo "JAVA_HOME = ${JAVA_HOME}"
  echo "JAVA_VERSION = $(java -version)"
  echo "***********************"
}
