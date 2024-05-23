#!/bin/sh

# 引入外部shell脚本
. ./function.sh

##################################
# info函数
##################################
info() {
  echo "System information:"
  echo "***********************"
  echo $(head -n 1 /etc/issue)
  echo $(uname -a)
  echo
  echo "JAVA_HOME = $JAVA_HOME"
  echo $(java -version)
  echo
  echo "***********************"
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
  esac
}

# 调用usage函数
usage "$1"
