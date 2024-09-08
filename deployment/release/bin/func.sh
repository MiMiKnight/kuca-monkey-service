#!/bin/bash
#set -ex

############全局变量常量############
# 脚本当前所在目录 常量
CONST_CURRENT_DIR=$(cd "$(dirname "$0")" && pwd)
declare -r CONST_CURRENT_DIR;
# 脚本所在的上一级目录 常量
CONST_PARENT_DIR=$(dirname "$CONST_CURRENT_DIR")
declare -r CONST_PARENT_DIR;
# shell内的JAVA_HOME环境变量
declare -x EVN_JAVA_HOME=""
# app_pid 全局变量
declare -i g_app_pid=0
# 主类名 全局变量
declare g_main_class="@app.main.class@"
# 应用jar包名称 全局变量
declare g_app_jar_path="${CONST_PARENT_DIR}/lib/@app.jar.name@.jar"
# 启动日志路径
declare g_app_startup_log_path="${CONST_PARENT_DIR}/log/startup.log"

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
# $1 参数1：APP MainClass（必填参数）
##################################
GetJavaPID() {
  local main_class=$1 jps_info='';
  jps_info=$("${EVN_JAVA_HOME}/bin/jps" -l | grep "${main_class}")
  if [ -n "${jps_info}" ]; then
    g_app_pid=$(echo "${jps_info}" | awk '{print $1}')
  else
    g_app_pid=0
  fi
}

##################################
# check应用PID 函数
# $1 参数1：APP MainClass（必填参数）
##################################
CheckJavaPID(){
  GetJavaPID "$1"
  if [ ${g_app_pid} -gt 0 ]; then
      Tip "Application already started !!! (pid=${g_app_pid})"
      exit 0
  fi
}

##################################
# start函数
##################################
Start() {
  # 检查应用是否已经启动
  CheckJavaPID ${g_main_class}

  local java_cmd='' java_opts='';
  # 启动超时时间，单位：秒
  local -i timeout=60;

  java_opts="-Xms512m \
  -Xmx1024m \
  -XX:MetaspaceSize=512m \
  -XX:MaxMetaspaceSize=1024m \
  -server \
  -Duser.language=en \
  -Duser.timezone=GMT+00:00 \
  -Dfile.encoding=utf-8 \
  -Dspring.profiles.name=application \
  -Dspring.profiles.active=debug"

  classpath=".:${CONST_PARENT_DIR}/lib:"

  java_cmd="nohup ${EVN_JAVA_HOME}/bin/java -jar ${java_opts} ${g_app_jar_path} > ${g_app_startup_log_path} 2>&1 &"
  `${java_cmd}`

  # 检查应用是否启动成功
  GetJavaPID ${g_main_class}
  if [ ${g_app_pid} -gt 0 ]; then
      Tip "Application start successfully !!! (pid=${g_app_pid})"
      exit 0
  else
      Warn "Application start failed !!!"
  fi
}

##################################
# stop函数
##################################
stop() {
  echo 'stop'
}

##################################
# restart函数
##################################
restart() {
  echo 'restart'
}

##################################
# status函数
##################################
status() {
  echo 'status'
}

##################################
# 健康检查函数
##################################
healthcheck(){
  exit 0
}

##################################
# info函数
##################################
info() {
  echo "System information:"
  echo "***********************"
  echo $(head -n 1 /etc/issue)
  echo $(uname -a)
  echo "JAVA_HOME = ${JAVA_HOME}"
  echo "JAVA_VERSION = $(java -version)"
  echo "***********************"
}
