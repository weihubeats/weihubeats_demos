## Timer

还记得之前介绍的jdk

Timer在netty中的本身只是一个接口

```java
public interface Timer {
    Timeout newTimeout(TimerTask task, long delay, TimeUnit unit);
    
    Set<Timeout> stop();
}
```

核心方法就两个
1. newTimeout 创建新任务
2. 停止所有未执行的任务

具体的实现类主要是`HashedWheelTimer`


## TimerTask

不同于jdk中`TimerTask`，netty中的`TimerTask`仅仅是一个接口，仅有一个方法
```java
public interface TimerTask {

    void run(Timeout timeout) throws Exception;

}
```

> jdk中的`TimerTask`是一个抽象类

## Timeout

创建任务后会提供一个任务的管理类`Timeout`

`Timeout`接口有如下方法可以用来管理任务
```java
public interface Timeout {

    // 获取Timer对象
    Timer timer();
    
    // 获取 TimerTask
    TimerTask task();
    
    // 任务是否已过期
    boolean isExpired();
    
    // 任务是否已经被取消
    boolean isCancelled();
    
    // 取消任务
    boolean cancel();
}
```
## 实战



