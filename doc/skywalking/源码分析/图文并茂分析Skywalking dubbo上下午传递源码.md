## 背景
最近在研究`dubbo`的`trace`上下文传递，所以免不了要看看`skywalking`这一快的实现原理

## skywalking version
- 8.12.0

## 插件版本

- dubbo-3.x-plugin


![dubbo-3.x-plugin-directory.png](..%2Fimages%2Fdubbo-3.x-plugin-directory.png)

## dubbo-3.x-plugin源码目录结构


![dubbo-3.x-plugin-directory.png](..%2Fimages%2Fdubbo-3.x-plugin-directory.png)

## 源码整体流程

![dubbo-3.x-plugin-workflows.png](..%2Fimages%2Fdubbo-3.x-plugin-workflows.png)


## 源码分析

可以看到主要有三个核心类 + 一个配置文件

### DubboInstrumentation

这个类主要定义了什么时候触发增强，增强哪个类的哪个方法

#### 增强哪个类

```java
public static final String ENHANCE_CLASS = "org.apache.dubbo.monitor.support.MonitorFilter";

@Override
protected ClassMatch enhanceClass() {
    return NameMatch.byName(ENHANCE_CLASS);
}
```

可以看到主要是增强`MonitorFilter`这个类

#### 什么情况触发增强

```java
public static final String CONTEXT_TYPE_NAME = "org.apache.dubbo.rpc.RpcContext";

public static final String GET_SERVER_CONTEXT_METHOD_NAME = "getServerContext";

public static final String CONTEXT_ATTACHMENT_TYPE_NAME = "org.apache.dubbo.rpc.RpcContextAttachment";

@Override
protected List<WitnessMethod> witnessMethods() {
    return Collections.singletonList(
        new WitnessMethod(
            CONTEXT_TYPE_NAME,
            named(GET_SERVER_CONTEXT_METHOD_NAME).and(
                returns(named(CONTEXT_ATTACHMENT_TYPE_NAME)))
        ));
}
```

这里主要定义了在`org.apache.dubbo.rpc.RpcContext`中必须存在方法`getServerContext`
并且他的返回值为`org.apache.dubbo.rpc.RpcContextAttachment`

我们简单对比下`2.7.x`的`dubbo`的增强条件是什么
```java

    private static final String CONTEXT_TYPE_NAME = "org.apache.dubbo.rpc.RpcContext";

    private static final String GET_SERVER_CONTEXT_METHOD_NAME = "getServerContext";

    @Override
    protected List<WitnessMethod> witnessMethods() {
        return Collections.singletonList(new WitnessMethod(
            CONTEXT_TYPE_NAME,
            named(GET_SERVER_CONTEXT_METHOD_NAME).and(
                returns(named(CONTEXT_TYPE_NAME)))
        ));
    }
```


主要是`org.apache.dubbo.rpc.RpcContext`类中的`getServerContext`方法，返回值也是`org.apache.dubbo.rpc.RpcContext`

那么很明确了`2.7`和`3.x`的`getServerContext`的返回值被修改了。

#### 增强的方法

在前面我知道增强的类是`MonitorFilter`。

现在我们具体来看看增强哪个方法

```java
@Override
public InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
    return new InstanceMethodsInterceptPoint[] {
        new InstanceMethodsInterceptPoint() {
            @Override
            public ElementMatcher<MethodDescription> getMethodsMatcher() {
                return named(INTERCEPT_POINT_METHOD);
            }

            @Override
            public String getMethodsInterceptor() {
                return INTERCEPT_CLASS;
            }

            @Override
            public boolean isOverrideArgs() {
                return false;
            }
        }
    };
}
```

要增强的方法是
```java
public static final String INTERCEPT_POINT_METHOD = "invoke";
```

实现增强的类是

```java
public static final String INTERCEPT_CLASS = "org.apache.skywalking.apm.plugin.asf.dubbo3.DubboInterceptor";
```

所以接下来我们来看看实际实现增强的逻辑吧

## DubboInterceptor


![dubbo-3.x-plugin-DubboInterceptor.png](..%2Fimages%2Fdubbo-3.x-plugin-DubboInterceptor.png)

源码有点多。我们来一一分析

1. 校验是不是Consumer

```java
boolean isConsumer = isConsumer(invocation);

private static boolean isConsumer(Invocation invocation) {
    Invoker<?> invoker = invocation.getInvoker();
    // As RpcServiceContext may not been reset when it's role switched from provider
    // to consumer in the same thread, but RpcInvocation is always correctly bounded
    // to the current request or serve request, https://github.com/apache/skywalking-java/pull/110
    return invoker.getUrl()
            .getParameter("side", "provider")
            .equals("consumer");
}
```

校验方式就通过`urlParam`获取`side`参数，默认为`provider`

2. 获取RPC上下文`RpcContextAttachment`

3. 如果是Consumer则创建ExitSpan，如果是Provider则创建EntrySpan

4. 如果是`Consumer`则将`skywalking`上下文载体`ContextCarrier`放入`dubbo`的`rpc`上下文`RpcContextAttachment`中,以便上下午在`dubbo`进行传递
```java
CarrierItem next = contextCarrier.items();
while (next.hasNext()) {
    next = next.next();
    attachment.setAttachment(next.getHeadKey(), next.getHeadValue());
    if (invocation.getAttachments().containsKey(next.getHeadKey())) {
        invocation.getAttachments().remove(next.getHeadKey());
    }
}
```

5. 如果是`Provider`则从`dubbo`上下文`RpcContextAttachment`中获取`Consumer`的上下文恢复到`skywalking`上下文`ContextCarrier`中

## 总结

总的来说`dubbo`这一快的上下文传递还是很简单的。总结为如下几个步骤

1. 是否需要增强
2. 如果是则判断是`Provider`还是`Consumer`
3. 如果是`Consumer`则将上下文放入`skywalking`上下文`ContextCarrier`中
4. 如果是如果是`Provider`则从`dubbo`上下文`RpcContextAttachment`中获取`Consumer`的上下文恢复到`skywalking`上下文`ContextCarrier`中

如果我们看其他实现`trace`传递的dubbo增强也是类似的实现原理，比如`Tlog`，感兴趣可以去看看，这里就不分析了