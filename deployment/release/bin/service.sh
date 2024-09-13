#!/bin/bash
set -euEx
# set -x 参数 作用：显示参数值（调试脚本时打开，平时注释）
##############全局变量##############
# 脚本当前所在目录
script_current_dir="$(cd "$(dirname "$0")" && pwd)"
# 项目目录
app_dir="$(dirname "${script_current_dir}")"
# 项目jar包路径
app_jar_location="${app_dir}/lib/@app.jar.name@.jar"
# 启动日志文件路径
app_startup_log_location="${app_dir}/logs/startup.log"
# 健康检查接口URL
health_check_url="https://127.0.0.1:8443/rest/developer/monkey-service/health/servlet/v1/check";
# 项目业务端口
app_port=8443
# JAVA_OPTS
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
}

#####################################
## trap signal 函数
#####################################
TrapSignal(){
  # 捕捉错误发生位置
  trap '$(TraceError $LINENO $BASH_COMMAND);exit 1' ERR
}

##################################
# 设置JAVA环境变量函数
# $1 参数1：JAVA安装目录（非必填参数）
##################################
GetJavaHome() {
  # java安装目录
  local java_install_dir=$1 java_program_location="";

  [ -e "${java_install_dir}/bin/java" ] && JAVA_HOME=${java_install_dir}
  [ ! -e "${JAVA_HOME}/bin/java" ] && JAVA_HOME=$HOME/jdk/java
  [ ! -e "${JAVA_HOME}/bin/java" ] && JAVA_HOME=/usr/java
  [ ! -e "${JAVA_HOME}/bin/java" ] && unset JAVA_HOME

  # 查找java执行程序路径
  java_program_location=$(which java)
  if [ -z "${java_program_location}" ] || [ ! -e "${java_program_location}" ]; then
    # 返回输出空字符串
    echo ""
  else
    java_install_dir=$(dirname "$java_program_location")
    java_install_dir=$(cd "$(dirname "${java_install_dir}")" && pwd)
    # 生成JAVA_HOME环境变量
    export JAVA_HOME=${java_install_dir}
    # 返回输出Java安装目录
    echo "${java_install_dir}"
  fi
}

##################################
# CheckEnv函数
##################################
CheckEnv(){
  if [ -z "$(GetJavaHome '')" ]; then
    Error "Please install Java and set environment variables, We need java(x64) and jdk8 or later is better !!!"
    exit 1
  fi
}

##################################
# GetJavaPid 函数
# $1 Java进程名
##################################
GetJavaPID(){
  local pid=-1 p_name=$1 jps_info="";
  jps_info=$(${JAVA_HOME}/bin/jps -l | grep "${p_name}")
  if [ -z "${jps_info}" ]; then
    echo "${pid}"
  else
    pid=$(echo "${jps_info}" | awk '{print $1}')
    echo "${pid}"
  fi
}

##################################
# CheckAlive 函数
# 描述：根据端口号和进程PID双重检测应用是否存活
# false：失活  true：存活
##################################
CheckAlive(){
  # 获取进程PID
  local pid=-1
  pid=$(GetJavaPID "${app_jar_location}")
  # pid = 0，表示程序未启动
  if [[ ${pid} -le -1 ]]; then
    echo false
    return
  fi
  local port=${app_port}
  # 再根据端口号检测
  local result="" port=0 p_name=""
  port=${app_port}
  p_name="${pid}/java"
  result=$(netstat -ntlp | awk -v p_name="${p_name}" '{ if($6=="LISTEN" && $7==p_name) print $4}' | grep "${port}")
  if [ -z "${result}" ]; then
     echo false
  else
     echo true
  fi
}

##################################
# start函数
##################################
Start() {
  # 捕捉脚本退出信号，杀死指定的后台Java进程
  #trap 'kill -9 $(GetJavaPID "${app_jar_location}")' exit
  CheckEnv
  # 检测程序是否已启动
  local pid=-1;
  pid=$(GetJavaPID "${app_jar_location}")
  # pid 大于0，表示程序已启动
  if [[ ${pid} -gt 0 ]]; then
    Info "the application has started and pid = ${pid} !!!"
    return 0
  fi
  # 启动应用
  nohup "${JAVA_HOME}/bin/java" ${java_opts} -jar "${app_jar_location}" > "${app_startup_log_location}" 2>&1
}

##################################
# stop函数
##################################
Stop() {
  CheckEnv
  local pid=0;
  # 检测程序是否已启动
  pid=$(GetJavaPID "${app_jar_location}")
  # pid 大于0，表示程序已启动
  if [[ ${pid} -gt 0 ]]; then
    kill -9 "${pid}"
    Info "the application stopped success and pid = ${pid} !!!"
  else
    Info "the application is not running !!!"
  fi
}

##################################
# 健康检查函数
##################################
HealthCheck(){
  local check_url="" http_code=0 success_response_http_status=200
  check_url="${health_check_url}"
  http_code=$(curl -s -k -X GET -w %{http_code} -o /dev/null "${check_url}");
  [[ ${http_code} -eq ${success_response_http_status} ]] || exit 1
}

##################################
# usage函数
##################################
usage() {
  TrapSignal
  case "$1" in
  'start')
    Start
    ;;
  'stop')
    Stop
    ;;
  'alive')
    CheckAlive
    ;;
  'healthcheck')
    HealthCheck
    ;;
  *)
    echo "usage: service [start|stop|healthcheck]"
    ;;
  esac
}

# 调用usage函数
usage "$1"