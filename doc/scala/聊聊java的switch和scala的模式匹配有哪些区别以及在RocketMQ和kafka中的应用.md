## 背景

最近在看kafka源码发现，`kafka`中`scala`语法用到了大量的模式匹配，所以就来简单研究下`scala`的模式匹配


## java switch使用

```java
        int xiaozou = 1;
        switch (xiaozou) {
            case 1:
                System.out.println("xiaozou = 1");
                break;
            case 2:
                System.out.println("xiaozou = 2");
                break;
            default:
                System.out.println("xiaozou = default");
        }

    }
```

`java`的`Switch`用法比较简单，也只能做到上面这种简单的判断，只支持单数据类型

## scala模式匹配

首先我们来看一个基础数据类型的例子

```scala

def main(args: Array[String]): Unit = {

    println(describe(42))
    println(describe(-10))
    println(describe("hello"))
    println(describe(List(1,2,3,4)))
    println(describe(Some("test")))
    println(describe(None))
    println(describe(3.14))


}

  def describe(x: Any): String = x match {
    case i: Int if i > 0 => s"Positive number: $i"
    case i: Int if i < 0 => s"Negative number: $i"
    case s: String => s"String with length ${s.length}"
    case list: List[_] if list.length > 3 => s"Long list of integers, first element: ${list.head}"
    case Some(value) => s"Some with value: $value"
    case None => "None"
    case _ => "Something else"
  }
  
```
- 运行结果
```
Positive number: 42
Negative number: -10
String with length 5
Long list of integers, first element: 1
Some with value: test
None
Something else
```

可以看到这里的模式匹配比`java`的`switch`要强大很多，可以匹配任何类型的数据，然后能做一些复杂的判断

对于自定义类型，也是可以实现很强大的模式匹配

```scala
  def main(args: Array[String]): Unit = {

    println(describeClass(XiaoZou("小奏", 15)))
    println(describeClass(XiaoZou("大奏", 25)))
    println(describeClass(Language("Max", "Labrador")))
    println(describeClass(Language("Buddy", "Beagle")))

}

def describeClass(clazz: Any): String = clazz match {
    case XiaoZou(name, age) if age < 18 => s"$name is a minor"
    case XiaoZou(name, age) => s"$name is an adult"
    case Language(name, "Java") => s"$name 比较熟悉"
    case Language(name, _) => s"$name 一般熟悉"
    case _ => "Unknown language"
}



case class  XiaoZou(name: String, age: Int)

case class Language(name: String, version: String)
```

- 运行结果
```
小奏 is a minor
大奏 is an adult
Java 一般熟悉
Go 一般熟悉
```

可以看到`scala`中的模式匹配可以提取出对象的属性，然后进行一些复杂的条件判断

## kafka中的模式匹配使用

`kafka`中大量使用了`scala`语法，模式匹配很场景，`kafka`最核心最终要的源码类`KafkaApis`对不同的网络请求处理就使用了模式匹配去处理

![kafka-scala-math.png](./images/kafka-scala-math.png)

我们再看看如果没有使用模式匹配对于这些case 用java 是如何处理的，我们这里看看`RocketMQ`这一快的源码

![rocketmq-switch.png](./images/rocketmq-switch.png)

其实这一部分来看不出来太大的语法差异，主要就是`scala`不需要显式的`break`

值得注意的是jdk17也支持了类似的功能，比如像如下
```java
String day = "MONDAY";
String result = switch (day) {
    case "MONDAY", "FRIDAY", "SUNDAY" -> "休息日";
    case "TUESDAY" -> "工作日";
    case "THURSDAY", "SATURDAY" -> "学习日";
    case "WEDNESDAY" -> "购物日";
    default -> "未知";
};

```


## 总结

总的来说`scala`的模式匹配比java的`switch`强很多，具体表现如下几个方面

- 可以匹配几乎任何类型的数据，包括复杂对象、集合和自定义类型
- 可以解构复杂对象，提取其中的字段或元素
- 可以包含复杂的条件逻辑，如类型检查、值范围检查等
- 通常没有落空行为，每个case都是独立的，不需要显式的break

但是随着jdk的慢慢升级，一些`scala`的语法都在慢慢支持