## 问题

执行`mvn`报错
```sehll
zsh: command not found: mvn
```

## 处理方法
1. 检查maven是否安装
2. 如果安装则添加环境变量
   1. 打开`~/.zshrc`文件
```shell
open ~/.zshrc
```
   2. 添加`export PATH=$PATH:/path/to/apache-maven-3.8.1/bin`注意替换`/path/to/apache-maven-3.8.1/bin`为你的maven安装路径
   3. 让环境变量生效
```shell
source ~/.zshrc
```