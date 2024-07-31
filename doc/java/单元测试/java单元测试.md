## 单测引用的依赖


```xml
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.2</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>5.12.0</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-junit-jupiter</artifactId>
            <version>5.12.0</version>
            <scope>compile</scope>
        </dependency>
```

如果是`spring boot`项目

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
```

## 对象校验

```java
public class MyList extends AbstractList<String> {
 
    @Override
    public String get(final int index) {
        return null;
    }
    @Override
    public int size() {
        return 0;
    }
}
```

### 校验调用了某个方法

```java
List<String> mockedList = mock(MyList.class);
mockedList.size();
verify(mockedList).size();
```

### 校验调用某个方法的次数

```java
List<String> mockedList = mock(MyList.class);
mockedList.size();
verify(mockedList, times(1)).size();
```

### 校验没有调用某个方法

```java
List<String> mockedList = mock(MyList.class);
verify(mockedList, times(0)).size();

List<String> mockedList = mock(MyList.class);
mockedList.size();
verify(mockedList, never()).clear();
```

### 校验等于某个值
```java
assertEquals(consumer.getMaxReconsumeTimes(), maxReconsumeTimes, "maxReconsumeTimes is not equal");
```

## 对象mock

```java
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        Assertions.assertThat(regionName).isEqualTo(REGION);
        when(resultSetMetaData.getColumnType(1)).thenReturn(Types.TIMESTAMP);
        when(resultSetMetaData.getColumnType(2)).thenReturn(Types.VARCHAR);
```
## mock静态方法
1. 添加依赖
```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-inline</artifactId>
    <version>3.4.0</version>
    <scope>test</scope>
</dependency>
```

2. mock静态方法

```java
public class UtilityClass {
    public static String staticMethod() {
        return "Original";
    }
}
```

```java
import org.mockito.MockedStatic;
import org.mockito.Mockito;

try (MockedStatic<UtilityClass> mockedStatic = Mockito.mockStatic(UtilityClass.class)) {
    mockedStatic.when(UtilityClass::staticMethod).thenReturn("Mocked");
    String result = UtilityClass.staticMethod();
    System.out.println(result); // 输出 "Mocked"
}
```