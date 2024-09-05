#!/bin/sh

# 引入外部shell脚本
. ./function.sh

##################################
# info函数
##################################
info() {
  echo "System information:"
  echo "***********************\n"
  echo $(head -n 1 /etc/issue)
  echo $(uname -a)
  echo "JAVA_VERSION = xxx"
  echo "JAVA_HOME = $JAVA_HOME"
  echo $(java -version)
  echo
  echo "***********************\n"
}

##################################
# usage函数
##################################
usage() {
  case "$1" in
  'info')
    info
    ;;
  'start')
    start
    ;;
  'stop')
    stop
    ;;
  'restart')
    restart
    ;;
  'status')
    status
    ;;
  *)
    echo "service [info|start|stop|restart|status]"
    ;;
  esac
}

# 调用usage函数
usage "$1"
