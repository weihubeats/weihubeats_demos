#!/bin/bash

# 确保脚本以root权限运行
if [ "$(id -u)" != "0" ]; then
   echo "该脚本必须以root权限运行" 1>&2
   exit 1
fi

# 更新文件描述符限制
echo "* soft nofile 100000" >> /etc/security/limits.conf
echo "* hard nofile 100000" >> /etc/security/limits.conf

# 更新虚拟内存设置 默认 60
sysctl -w vm.swappiness=1
echo "vm.swappiness=1" >> /etc/sysctl.conf

# 更新最大内存映射数量 默认 65530
sysctl -w vm.max_map_count=262144
echo "vm.max_map_count=262144" >> /etc/sysctl.conf

# 更新网络设置
sysctl -w net.ipv4.tcp_fin_timeout=30
sysctl -w net.ipv4.tcp_keepalive_intvl=30
sysctl -w net.ipv4.tcp_keepalive_probes=5
sysctl -w net.ipv4.tcp_keepalive_time=120
sysctl -w net.core.somaxconn=8192

echo "net.ipv4.tcp_fin_timeout=30" >> /etc/sysctl.conf
echo "net.ipv4.tcp_keepalive_intvl=30" >> /etc/sysctl.conf
echo "net.ipv4.tcp_keepalive_probes=5" >> /etc/sysctl.conf
echo "net.ipv4.tcp_keepalive_time=120" >> /etc/sysctl.conf
echo "net.core.somaxconn=8192" >> /etc/sysctl.conf

# 更新最大线程数限制
echo "* soft nproc 4096" >> /etc/security/limits.d/90-nproc.conf
echo "* hard nproc 4096" >> /etc/security/limits.d/90-nproc.conf

echo "系统调优完成。可能需要重启以应用某些更改。"