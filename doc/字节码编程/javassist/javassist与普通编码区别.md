## 类型加载问题

大部分自定义类的使用需要使用类的完全限定名
正常编码
```java
XiaoZou xiaozou = new XiaoZou();
```

字节码编码需要使用全限定名
```java
com.weihubeats.XiaoZou xiaozou = new com.weihubeats.XiaoZou();
```

## 基本类型装箱拆箱问题

javassist 不存在自动装箱、拆箱。必须显式进行装箱和拆箱操作

比如以下方式是错误的
```java
Object o = 5; 
```

正确写法
```java
int v = 5;
Object o = new Integer(v);  
```

类型强制转换也会报错。比如这样
```java
Object v = 5;
int n = (int)v; 
```

javassist应该手动进行类型转换
```java

Object v = 5;
int n = ((Number)v).intValue()
```

所以类型转换这种麻烦的操作推荐都封装好在一个类中，然后进修字节码的编程的时候调用相应的方法即可

## 函数接口(匿名内部类)
正常编程
```java

Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }

    };

```

javassist需要有具体的实现类
```java

class MyCallable implements Callable {
 
        public Object call() throws Exception {
            return null;
        }
}
```
