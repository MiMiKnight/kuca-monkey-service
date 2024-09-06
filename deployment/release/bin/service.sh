#!/bin/sh

# 引入外部shell脚本
. ./function.sh

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
