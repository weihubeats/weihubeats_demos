
```shell
#!/bin/bash

# 确保脚本以root权限运行
if [ "$(id -u)" != "0" ]; then
   echo "该脚本必须以root权限运行" 1>&2
   exit 1
fi

# 限制指定了单个进程可以打开的最大文件数 更新文件描述符限制 默认值通常较低，例如1024（软限制）和4096（硬限制）
echo "* soft nofile 100000" >> /etc/security/limits.conf
echo "* hard nofile 100000" >> /etc/security/limits.conf

# 控制内核将多少空闲的内存页面交换到磁盘上。值范围从0到100，较低的值减少了交换操作，较高的值更积极地使用交换。对于内存密集型的应用，推荐设置为较低的值（如1），以减少交换活动，因为交换操作可能会导致显著的延迟 更新虚拟内存设置 默认值通常是60，这意味着内核会相对积极地使用交换空间。
sysctl -w vm.swappiness=1
echo "vm.swappiness=1" >> /etc/sysctl.conf

# 进程可以拥有的最大内存映射区域数量。对于使用大量内存映射文件的应用，增加这个值可以提高性能, 默认值通常是65530，这是系统允许的最大内存映射区域数量。
sysctl -w vm.max_map_count=262144
echo "vm.max_map_count=262144" >> /etc/sysctl.conf

# 更新网络设置

#  这个参数定义了等待socket连接中FIN包的时间（单位是秒）。减少这个值可以帮助系统更快地回收处于TIME_WAIT状态的socke 默认值通常是60秒
sysctl -w net.ipv4.tcp_fin_timeout=30
#  允许重新使用处于TIME_WAIT状态的socket用于新的TCP连接，这对于快速打开和关闭连接的应用很有用 默认值是0，表示不重用处于TIME_WAIT状态的socket
sysctl -w net.ipv4.tcp_tw_reuse=1
# TCP socket接收缓冲区大小 默认值可能是212992字节。 
sysctl -w net.core.rmem_max=16777216
# TCP socket发送缓冲区大小 默认值可能是212992字节。
sysctl -w net.core.wmem_max=16777216
# 系统在接收到第一个SYN包时可以排队的最大请求数量。增加这个值可以允许更多的并发连接请求 默认值可能在128到1024之间
sysctl -w net.ipv4.tcp_max_syn_backlog=8096
# socket监听队列的最大长度。增加这个值对于高并发的TCP应用很有帮助 默认值通常是128，这是socket监听队列的最大长度
sysctl -w net.core.somaxconn=8192
#  在接口接收数据包的速率比内核处理速率快时，允许进入队列的数据包的最大数目 默认值通常是1000
sysctl -w net.core.netdev_max_backlog=4096
# TCP socket的接收和发送缓冲区的最小值、默认值和最大值 默认值可能是4096 87380 6291456，分别对应最小值、默认值和最大值
sysctl -w net.ipv4.tcp_rmem='4096 87380 16777216'
sysctl -w net.ipv4.tcp_wmem='4096 65536 16777216'

#  控制了TCP重新发送keepalive探测消息的时间间隔 默认值分别通常是75秒。
sysctl -w net.ipv4.tcp_keepalive_intvl=30
# 认定连接断开前发送探测消息的次数 默认9次
sysctl -w net.ipv4.tcp_keepalive_probes=5
# TCP发送keepalive探测消息的时间间隔，用来检测死连接或错误状态 默认值通常是7200秒（2小时）
sysctl -w net.ipv4.tcp_keepalive_time=120

echo "net.ipv4.tcp_fin_timeout=30" >> /etc/sysctl.conf
echo "net.ipv4.tcp_tw_reuse=1" >> /etc/sysctl.conf
echo "net.core.rmem_max=16777216" >> /etc/sysctl.conf
echo "net.core.wmem_max=16777216" >> /etc/sysctl.conf
echo "net.ipv4.tcp_max_syn_backlog=8096" >> /etc/sysctl.conf
echo "net.core.somaxconn=8192" >> /etc/sysctl.conf
echo "net.core.netdev_max_backlog=4096" >> /etc/sysctl.conf
echo "net.ipv4.tcp_rmem=4096 87380 16777216" >> /etc/sysctl.conf
echo "net.ipv4.tcp_wmem=4096 65536 16777216" >> /etc/sysctl.conf
echo "net.ipv4.tcp_keepalive_intvl=30" >> /etc/sysctl.conf
echo "net.ipv4.tcp_keepalive_probes=5" >> /etc/sysctl.conf
echo "net.ipv4.tcp_keepalive_time=120" >> /etc/sysctl.conf

# 更新最大线程数限制
# 默认值取决于系统设置和具体的Linux发行版，某些系统可能设置为1024
echo "* soft nproc 4096" >> /etc/security/limits.d/90-nproc.conf
echo "* hard nproc 4096" >> /etc/security/limits.d/90-nproc.conf

# 应用sysctl设置
sysctl -p

echo "系统调优完成。可能需要重启以应用某些更改。"
```

文件描述符限制
默认值通常较低，例如1024（软限制）和4096（硬限制），但这些值可以根据系统配置和需求进行调整。
虚拟内存设置
vm.swappiness: 默认值通常是60，这意味着内核会相对积极地使用交换空间。
vm.max_map_count: 默认值通常是65530，这是系统允许的最大内存映射区域数量。
网络设置
net.ipv4.tcp_fin_timeout: 默认值通常是60秒。
net.ipv4.tcp_tw_reuse: 默认值是0，表示不重用处于TIME_WAIT状态的socket。
net.core.rmem_max 和 net.core.wmem_max: 默认值可能是212992字节。
net.ipv4.tcp_max_syn_backlog: 默认值可能在128到1024之间，具体取决于系统配置。
net.core.somaxconn: 默认值通常是128，这是socket监听队列的最大长度。
net.core.netdev_max_backlog: 默认值通常是1000。
net.ipv4.tcp_rmem 和 net.ipv4.tcp_wmem: 默认值可能是4096 87380 6291456，分别对应最小值、默认值和最大值。
net.ipv4.tcp_keepalive_time: 默认值通常是7200秒（2小时）。
net.ipv4.tcp_keepalive_intvl 和 net.ipv4.tcp_keepalive_probes: 默认值分别通常是75秒和9次。
最大线程数限制
nproc: 默认值取决于系统设置和具体的Linux发行版，某些系统可能设置为1024。