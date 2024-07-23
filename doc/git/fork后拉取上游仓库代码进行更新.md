
1. 检查远程同步仓库
```shell
git remote -v
```

2. 新增上游仓库地址
```shell
git remote add upstream https://github.com/apache/rocketmq
```

3. 再次查看远程仓库地址
```shell
git remote -v
```

> 可以看到添加上游仓库地址成功

4. 抓取upstream仓库的最新代码
```shell
git fetch upstream
```

5. 合并upstream仓库的代码
```shell
git merge upstream/master
```