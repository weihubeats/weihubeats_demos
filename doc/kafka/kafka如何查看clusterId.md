## 背景
一般新建`kafka`集群的时候会使用脚本生成一个集群唯一UUID。如果要新增`broker`就需要使用这个集群id进行存储路径初始化

所以需要查找原来集群的UUID(clusterId)是啥

## 如何查看

一般集群相关的元数据存储在`meta.properties`这个文件里面。

存储的路径和log文件保持一致，所以文件路径就是配置在`log.dir`中

文件大致是这样
```properties
#
#Sun Oct 08 11:07:39 CST 2023
node.id=4
version=1
cluster.id=mzZehZx0RNmke32QRMpNk
```