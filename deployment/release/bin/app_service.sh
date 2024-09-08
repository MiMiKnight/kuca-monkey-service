#!/bin/bash
#set -ex
# 脚本当前所在目录
current_dir=$(cd "$(dirname "$0")" && pwd)
# 当前脚本所在目录的上一级目录
parent_dir=$(dirname "$current_dir")
# 引入外部shell脚本
. func.sh

##################################
# usage函数
##################################
usage() {
  case "$1" in
  'info')
    Info
    ;;
  'start')
    Start
    ;;
  'stop')
    Stop
    ;;
  'restart')
    Restart
    ;;
  'status')
    Status
    ;;
  'healthcheck')
    HealthCheck
    ;;
  *)
    echo "args [info|start|stop|restart|status|healthcheck]"
    ;;
  esac
}

# 调用usage函数
usage "$1"
