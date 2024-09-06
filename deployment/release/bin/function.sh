#!/bin/sh
set -ex
##################################
# 正常提示函数
##################################
tip() {
  echo "TIP: $1"
  exit 0
}

##################################
# 告警提示函数
##################################
warn() {
  echo "WARN: $1"
  exit 0
}

##################################
# 错误提示退出函数
##################################
error_exit() {
  echo "ERROR: $1"
  exit 1
}

##################################
# 设置JAVA环境变量函数
# $1 参数1：JAVA安装目录（非必填参数）
##################################
set_java_home() {
  var_java_install_path=$1

  [ -e "$var_java_install_path/bin/java" ] && JAVA_HOME=$var_java_install_path
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
  p_app_mainclass=$1
  p_psid=0
  javaps=$($JAVA_HOME/bin/jps -l | grep $p_app_mainclass)
  if [ -n "$javaps" ]; then
    p_psid=$(echo $javaps | awk '{print $1}')
  else
    p_psid=0
  fi
  return $p_psid
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
