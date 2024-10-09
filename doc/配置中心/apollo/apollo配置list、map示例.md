## List


### 配置文件

```properties
test.list = test1,xiaozou,test2
```

### java代码

```java
	@Value("${test.list}")
    private List<String> list;
```

## Map

### 配置文件

```properties
test.map = {“test”: “vlaue1,vlaue2”, “xiaozou”: “xiaozouValue,xiaozouValue1”}
```
### java代码

```java
	@Value("#{${test.map}}")
    private Map<String, List<String>> testMap;
```
