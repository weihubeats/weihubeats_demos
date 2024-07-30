
1. 查找spring上下文
```shell
sc -d org.springframework.context.ApplicationContext
```

2. 基于vmtool查找bean
```shell
vmtool --action getInstances -c 73c48264 --className org.springframework.context.ApplicationContext --express 'instances[0].getBean("authFilter")' -x 3
```