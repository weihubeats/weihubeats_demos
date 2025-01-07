1.  查看端口占用进程pid

```shell
lsof -i tcp:port
```

> 将port换成被占用的端口(如：8086、9998)
> 将会出现占用端口的进程信息。

1.  杀掉该进程

```shell
kill PID
```

