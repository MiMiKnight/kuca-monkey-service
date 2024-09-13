#!/bin/bash
set -euE
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
# 项目业务端口
app_port=8443
# 健康检查接口URL
health_check_url="https://127.0.0.1:${app_port}/rest/developer/monkey-service/health/servlet/v1/check";
# 运行配置 从环境变量${JAVA_OPTS}中读取额外的运行时配置信息
run_opts="-Xms512m \
  -Xmx1024m \
  -XX:MetaspaceSize=512m \
  -XX:MaxMetaspaceSize=1024m \
  -server \
  -Duser.language=en \
  -Duser.timezone=GMT+00:00 \
  -Dfile.encoding=utf-8 \
  -Dspring.profiles.name=application \
  ${JAVA_OPTS}"

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

##################################
# 检查JAVA环境变量 函数
# $1 参数1：JAVA安装目录（非必填参数）
##################################
CheckJavaHome() {
  # java安装目录
  local java_location="";

  [ ! -x "${JAVA_HOME}/bin/java" ] && JAVA_HOME=$HOME/jdk/java
  [ ! -x "${JAVA_HOME}/bin/java" ] && JAVA_HOME=/usr/java
  [ ! -x "${JAVA_HOME}/bin/java" ] && JAVA_HOME=/opt/app/jdk
  [ ! -x "${JAVA_HOME}/bin/java" ] && unset JAVA_HOME && JAVA_HOME=""

  if [ -x "${JAVA_HOME}/bin/java"  ]; then
     return
  fi

  # 查找java执行程序路径
  java_location=$(which java)
  if [ -z "${java_location}" ] || [ ! -x "${java_location}" ]; then
    Error "please install Java or check environment variables !!!"
    exit 1
  else
    JAVA_HOME=$(dirname "${java_location}")
    JAVA_HOME=$(cd "$(dirname "${JAVA_HOME}")" && pwd)
    # 导出JAVA_HOME环境变量
    export JAVA_HOME=${JAVA_HOME}
  fi
}

##################################
# GetJavaPID 函数
##################################
GetJavaPID(){
  local p_name=$1 jps_info="";
  jps_info=$(${JAVA_HOME}/bin/jps -l | grep "${p_name}")
  if [ -z "${jps_info}" ]; then
     echo "0"
  else
     echo $(echo "${jps_info}" | awk '{print $1}')
  fi
}

##################################
# IsAlive 函数
# 描述：根据端口号和进程PID双重检测应用是否存活
# false：失活  true：存活
##################################
IsAlive(){
  # 获取进程PID
  local pid=$(GetJavaPID "${app_jar_location}")
  # pid = 0，表示程序未启动
  if [ ${pid} -eq "0" ]; then
    echo "false"
    return
  fi
  local port=${app_port}
  # 再根据端口号检测
  local result="" port=0 p_name=""
  port=${app_port}
  p_name="${pid}/java"
  result=$(netstat -ntlp | awk -v p_name="${p_name}" '{ if($6=="LISTEN" && $7==p_name) print $4}' | grep "${port}")
  if [ -z "${result}" ]; then
     echo "false"
  else
     echo "true"
  fi
}

##################################
# Status 函数
##################################
Status(){
  local pid=$(GetJavaPID "${app_jar_location}")
  local alive=$(IsAlive)
  Info "#################################"
  Info "alive status = ${alive}"
  Info "application pid = ${pid}"
  Info "#################################"
}

##################################
# start函数
##################################
Start() {
  # 检测程序是否已启动
  local pid=$(GetJavaPID "${app_jar_location}")
  # pid 不等于0，表示程序已启动
  if [ ${pid} -ne "0" ]; then
    Info "the application has started and pid = ${pid} !!!"
    return
  fi
  # 启动应用
  #nohup "${JAVA_HOME}/bin/java" ${run_opts} -jar "${app_jar_location}" > "${app_startup_log_location}" 2>&1
  nohup "${JAVA_HOME}/bin/java" ${run_opts} -jar "${app_jar_location}"
}

##################################
# stop函数
##################################
Stop() {
  local pid=$(GetJavaPID "${app_jar_location}")
  # 检测程序是否已启动
  if [ $(IsAlive) -eq "true" ]; then
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
  CheckJavaHome ""
  case "$1" in
  'start')
    Start
    ;;
  'stop')
    Stop
    ;;
  'status')
    Status
    ;;
  'healthcheck')
    HealthCheck
    ;;
  *)
    echo "usage: service [start|stop|status|healthcheck]"
    ;;
  esac
}

# 调用usage函数
usage "$1"