#!/bin/bash
set -euE
# set -x 参数 作用：显示参数值（调试脚本时打开，平时注释）
#################################

##############全局变量##############
# 脚本当前所在目录
script_current_dir=$(cd "$(dirname "$0")" && pwd)
# 程序锁文件路径
lock_file_location="${script_current_dir}/deploy-bohpdqmvyxoflyqt310u.lock"

##################################
# 友好提示函数
##################################
Info() {
  now=$(date +'%Y-%m-%d %H:%M:%S')
  echo -e "\e[1;90;49m[${now}] \e[1;32;49m[INFO] \e[1;39;49m$1\e[0m";
}

##################################
# 警告提示函数
##################################
Warn() {
  now=$(date +'%Y-%m-%d %H:%M:%S')
  echo -e "\e[1;90;49m[${now}] \e[1;33;49m[WARN] \e[1;39;49m$1\e[0m";
}

##################################
# 错误提示退出函数
##################################
Error() {
  now=$(date +'%Y-%m-%d %H:%M:%S')
  echo -e "\e[1;90;49m[${now}] \e[1;31;49m[ERROR] \e[1;39;49m$1\e[0m";
}

#####################################
## trace error 函数
## 显示错误位置，打印错误内容
#####################################
TraceError(){
  Error "script: $0 ,error on line: $1 command: '$2'"
  exit 0
}

#####################################
## trap signal 函数
#####################################
TrapSignal(){
  # 捕捉信号，清理任务;脚本解锁
  trap 'Clean;Unlock;exit 0;' EXIT HUP INT QUIT TSTP
  # 捕捉错误发生位置
  trap 'TraceError $LINENO $BASH_COMMAND' ERR
}

#####################################
## lock 函数
#####################################
Lock(){
  if [ -f "${lock_file_location}" ]; then
     Warn "there are already running build task. please try again later !!!"
     # 脚本退出执行
     exit 0
  else
     # 创建锁文件
     touch "${lock_file_location}"
     chattr +i "${lock_file_location}"
  fi
}

#####################################
## unlock 函数
#####################################
Unlock(){
  if [ -f "${lock_file_location}" ]; then
     chattr -i "${lock_file_location}"
     rm -rf "${lock_file_location}"
     exit 0
  fi
}


#####################################
## Run 函数
#####################################
Run(){
  TrapSignal
  Lock

}
Run