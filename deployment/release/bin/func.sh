#!/bin/bash
#set -ex
# 脚本当前所在目录
current_dir=$(cd "$(dirname "$0")" && pwd)
# 当前脚本所在目录的上一级目录
parent_dir=$(dirname "$current_dir")

##################################
# 友好提示函数
##################################
tip() {
  echo "[TIP]: $1"
}

##################################
# 警告提示函数
##################################
warn() {
  echo "[WARN]: $1"
}

##################################
# 错误提示退出函数
##################################
error_exit() {
  echo "[ERROR]: $1"
  exit 1
}

##################################
# 设置JAVA环境变量函数
# $1 参数1：JAVA安装目录（非必填参数）
##################################
java_home() {
  java_install_path=$1
  [ -e "$java_install_path/bin/java" ] && JAVA_HOME=$java_install_path
  [ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=$HOME/jdk/java
  [ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/java
  [ ! -e "$JAVA_HOME/bin/java" ] && unset JAVA_HOME

  if [ -z "$JAVA_HOME" ]; then
    JAVA_PATH=$(which java)
    if [ -z "$JAVA_PATH" ]; then
      error_exit "Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better!"
    else
      JAVA_HOME=$(dirname "$JAVA_PATH")
      JAVA_HOME=$(cd "$JAVA_HOME" && pwd)
      export PATH=$JAVA_HOME/bin:$PATH
    fi
  else
    JAVA_HOME=$(cd "$JAVA_HOME" && pwd)
    export PATH=$JAVA_HOME/bin:$PATH
  fi
}

##################################
# 获取应用PID的函数
# $1 参数1：APP MainClass
##################################
get_app_pid() {
  local pid;
  p_app_mainclass=$1
  pid=0
  javaps=$($JAVA_HOME/bin/jps -l | grep $p_app_mainclass)
  if [ -n "$javaps" ]; then
    pid=$(echo $javaps | awk '{print $1}')
  else
    pid=0
  fi
  return $pid
}

##################################
# start函数
# $1 参数1：APP MainClass
##################################
start() {
  p_app_mainclass=$1
  p_psid=$(get_app_pid $p_app_mainclass)

  if [ $p_psid -ne 0 ]; then
    tip_exit "$p_app_mainclass already started! (pid=$p_psid)"
  else
    echo "Starting $APP_MAINCLASS ..."
    JAVA_CMD="nohup $JAVA_HOME/bin/java $JAVA_OPTS -classpath $CLASSPATH $APP_MAINCLASS >/dev/null 2>&1 &"
    su - $RUN_USER -c "$JAVA_CMD"
    checkpid
    if [ $p_psid -ne 0 ]; then
      echo "(pid=$p_psid) [OK]"
    else
      echo "[Failed]"
    fi
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
