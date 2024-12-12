## 背景

相信初学`spring boot`的同学在各大培训视频或者教程中都会教你如何编写接口。

比如我们要写一个接口很简单


```java
@RestController
@RequestMapping("")
@Slf4j
public class XiaoZouController {


    @GetMapping("/xiaozou")
    public String testByXiaoZouJiShu(TestDTO testDTO) {
        return testDTO;
    }
}
```


加几个注解，一个接口就完成了。

如果要测试我们一般就是通过`postman`或者`apifox`来测试。

使用这两种方式测试有个缺点就是测试用例很难复用，其次无法在ci/cd中进行自动化测试。

> 部分api管理工具支持保存测试用例，但是始终是和代码分离的，不利于维护

所以为了接口的测试，我们需要编写集成测试。

遗憾的是很多开发很少了解在`spring boot`中如何编写集成测试，接下来我们就来详细讨论下`spring boot`种的集成测试

## 集成测试

在`spring boot`中集成测试的方式有很多，我们接下来一一讨论，看看哪种方式最合适、最优雅

### 手动注入controller进行测试

`spring boot`提供了`@SpringBootTest`注解来进行集成测试。

一般人测试`controller`都会这样测试

```java
@SpringBootTest
@Profile("dev")
class XiaoZouController {

    @Autowired
    private XiaoZouController xiaoZouController;

    @Test
    public void testController() {
        TestDTO testDTO = new TestDTO();
        String xiaoZouJiShu = xiaoZouController.testByXiaoZouJiShu(testDTO);
        assertEquals("xiaoZouJiShu", xiaoZouJiShu);
    }

}

```


使用这种方式进行测试有几个缺点

1. 无法模拟`http`请求
2. 仅能测试`controller`的代码逻辑，实际测试也就是`server`的逻辑
3. 像一些`http`中比如`header`、`cookie`等信息无法模拟，比如我们有一些切面进行了请求头的处理，比如我们从`header`中获取了`uid`等信息，这种情况下我们就无法进行测试

### 基于TestRestTemplate进行测试

如果我们想要模拟`http`请求，我们可以使用`TestRestTemplate`来进行测试

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("dev")
public class XiaoZouController {

    @LocalServerPort
    private int port;


    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void greetingShouldReturnDefaultMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/xiaozou", String.class)).contains("xiaozou");
    }
    
}
```

1. `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)`表示随机端口启动
2. `@LocalServerPort`表示获取随机端口
3. 使用`TestRestTemplate`是来模拟`http`请求的调用

这种方式测试也有缺点

1. 启动了一个本地服务器，启动慢
2. 对于结果校验没有提供开箱即用的断言机制

### 基于MockMvc进行测试

如果我们想要本地不启动服务器，只是测试服务器下面的层次，我们可以使用`MockMvc`来进行测试

这与处理真实的`http`请求处理方式一样，但是没有服务器启动成本

测试代码如下


```java
@SpringBootTest
@AutoConfigureMockMvc
@Profile("dev")
public class XiaoZouControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void greetingShouldReturnDefaultMessage() throws Exception {
        Long uid = 12345L;

        mockMvc.perform(MockMvcRequestBuilders.get("/xiaozou")
                .header("uid", uid)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.uid").value(uid))
            .andExpect(jsonPath("$.data.version").doesNotExist())
            .andExpect(jsonPath("$.data.deviceId").value(IsNull.nullValue()));
    }

}

```

1. 添加`@AutoConfigureMockMvc`注解自动注入`MockMvc`
2. 通过`@Autowired`注入`MockMvc`对象
3. 通过`mockMvc`进行`http`请求模拟
4. 通过`.hearder`模拟`header`信息
5. 通过`.contentType`模拟`content-type`信息
6. 通过`.andExpect`进行结果校验
7. 通过`jsonPath`进行`json`结果校验


### 总结

`spring boot`中测试`controller`有多种方式。

但是我觉得最好的方式还是通过`@AutoConfigureMockMvc`进行测试

不用启动服务器，测试速度快，而且可以模拟`http`请求，对于结果校验也提供了很多的断言机制。

对于一些经常变化的业务接口写集成测试的意义不是特别大。

是否需要编写集成测试还是要看接口的重要程度、项目的时间等多方面因素来决定。

小伙子，刀给你了，什么时候用你自己决定啦


## 参考

- https://spring.io/guides/gs/testing-web