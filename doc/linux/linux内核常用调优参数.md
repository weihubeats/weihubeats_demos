1. vm.swappiness：该参数控制系统在内存不足时，内核将页面交换到磁盘的程度。默认值为60，建议值为10-30。
2. vm.overcommit_memory：该参数控制系统是否允许超额分配内存。默认值为0，建议值为1。
3. vm.dirty_ratio：该参数控制系统脏页占内存的比例。默认值为20，建议值为5-10。
4. vm.dirty_background_ratio：该参数控制系统后台写入脏页的比例。默认值为10，建议值为1-5。
5. vm.dirty_expire_centisecs：该参数控制系统脏页过期时间。默认值为3000，建议值为1000-2000。
6. vm.dirty_writeback_centisecs：该参数控制系统写回脏页的时间间隔。默认值为500，建议值为100-200。
7. vm.vfs_cache_pressure：该参数控制系统内核缓存的大小和清理频率。默认值为100，建议值为50-100。
8. vm.min_free_kbytes：该参数控制系统保留的最小空闲内存。默认值为4096，建议值为65536。
9. vm.max_map_count：该参数控制系统允许的最大内存映射数量。默认值为65530，建议值为262144。
10. net.core.somaxconn：该参数控制系统TCP连接的最大排队数量。默认值为128，建议值为1024。
11. net.core.netdev_max_backlog：该参数控制系统网络设备接收数据包的队列大小。默认值为1000，建议值为5000。
12. net.core.rmem_max：该参数控制系统TCP接收缓冲区的最大大小。默认值为212992，建议值为524288。
13. net.core.wmem_max：该参数控制系统TCP发送缓冲区的最大大小。默认值为212992，建议值为524288。
14. net.ipv4.tcp_fin_timeout：该参数控制系统TCP连接关闭的超时时间。默认值为60，建议值为10-20。
15. net.ipv4.tcp_tw_reuse：该参数控制系统是否允许重用TIME_WAIT状态的TCP连接。默认值为0，建议值为1。
16. net.ipv4.tcp_tw_recycle：该参数控制系统是否启用TCP连接回收机制。默认值为0，建议值为1。
17. net.ipv4.tcp_max_syn_backlog：该参数控制系统TCP连接请求队列的大小。默认值为128，建议值为1024。
18. net.ipv4.tcp_keepalive_time：该参数控制系统TCP连接的保持时间。默认值为7200秒，建议值为600-1200。
19. net.ipv4.tcp_max_tw_buckets：该参数控制系统可以处理的TIME_WAIT状态的TCP连接的最大数量。默认值为180000，建议值为262144。
20. net.ipv4.ip_local_port_range：该参数控制系统可以使用的本地端口范围。默认值为32768-61000，建议值为1024-65535。
21. net.ipv4.tcp_slow_start_after_idle：该参数控制系统TCP连接空闲一段时间后是否重新进入慢启动状态。默认值为1，建议值为0。
22. net.ipv4.tcp_no_metrics_save：该参数控制系统是否保存TCP连接的性能指标。默认值为0，建议值为1。
23. net.ipv4.tcp_mtu_probing：该参数控制系统是否启用TCP MTU探测。默认值为0，建议值为1。
24. net.ipv4.tcp_congestion_control：该参数控制系统TCP拥塞控制算法。默认值为cubic，建议值为bbr。
25. fs.file-max：该参数控制系统可以打开的文件句柄数量。默认值为65536，建议值为1048576。
26. fs.nr_open：该参数控制系统可以打开的文件句柄数量。默认值为1048576，建议值为1048576。
27. fs.inotify.max_user_watches：该参数控制系统可以监视的文件数量。默认值为8192，建议值为524288。
28. kernel.sem：该参数控制系统信号量的数量。默认值为250，建议值为512-1024。
29. kernel.shmmax：该参数控制系统的共享内存大小。默认值为4294967295，建议值为536870912。
30. kernel.shmall：该参数控制系统的共享内存大小。默认值为2097152，建议值为134217728。
31. kernel.pid_max：该参数控制系统可以创建的最大进程数。默认值为32768，建议值为524288。
32. kernel.core_pattern：该参数控制系统在出现核心转储文件时的文件名格式。默认值为core，建议值为/corefiles/core-%e-%s-%u-%g-%p-%t。
33. kernel.msgmnb：该参数控制系统消息队列的最大大小。默认值为16384，建议值为65536。
34. kernel.msgmax：该参数控制系统消息队列的最大大小。默认值为8192，建议值为65536。
35. kernel.sysrq：该参数控制系统是否允许使用SysRq键。默认值为1，建议值为0。
36. kernel.printk：该参数控制系统内核日志的输出级别。默认值为4 4 1 7，建议值为3 3 3 3。
37. kernel.randomize_va_space：该参数控制系统是否启用地址空间随机化。默认值为2，建议值为2。
38. kernel.nmi_watchdog：该参数控制系统是否启用NMI watchdog。默认值为1，建议值为0。
39. kernel.softlockup_panic：该参数控制系统是否在软锁定时触发内核崩溃。默认值为0，建议值为1。
40. kernel.hung_task_panic：该参数控制系统是否在任务超时时触发内核崩溃。默认值为0，建议值为1。
41. kernel.panic：该参数控制系统在内核崩溃时的行为。默认值为0，建议值为10。
42. kernel.panic_on_oops：该参数控制系统在Oops发生时是否触发内核崩溃。默认值为0，建议值为1。
43. kernel.exec-shield：该参数控制系统是否启用执行保护。默认值为1，建议值为1。
44. kernel.dmesg_restrict：该参数控制系统是否限制非特权用户访问dmesg。默认值为1，建议值为1。
45. kernel.kptr_restrict：该参数控制系统是否限制非特


> 原文 https://www.cnblogs.com/yaoqingzhuan/p/17507916.html