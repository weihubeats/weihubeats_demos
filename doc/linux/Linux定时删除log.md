## 编写删除脚本

```sh
logPath=/data/

cd $logPath


echo "" > xiaozou/nohup.out

```

## 配置定时任务


1. 执行`crontab -e`

2. 编辑新增定时任务
```
*/30 * * * * sh /data/clear.sh
```

## 查看定时任务执行log

```sh
tail -f /var/log/cron
```