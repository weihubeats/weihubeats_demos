
 注解|作用|
 :--:|:--:|
@SpringBootApplication|标记`Spring`应用程序的主类。组合`@Configuration`、`@EnableAutoConfiguration`和`@ComponentScan`。这是您的应用程序开始的地方
@Component|将类标记为Spring组件，使其成为托管bean
@Service|服务层类的特殊类型@Component，通常包含业务逻辑
@Repository|用于数据仓库类的特殊类型的 @Component。启用异常转换
@Controller|用于处理 Web 请求的 Spring MVC 控制器的特殊类型 @Component。
@RestController	|结合了 @Controller 和 @ResponseBody。处理 Web 请求并直接返回 JSON/XML 响应
@Autowired	|自动将依赖项注入到字段、setter 方法或构造函数中
@Value|将属性文件中的值插入字段、setter方法或构造函数参数
@Configuration	|指示一个类声明一个或多个@Bean方法，并且可以被Spring容器处理以生成bean定义和服务请求
@Bean|声明一个方法，该方法返回Spring容器管理的Spring bean
@EnableAutoConfiguration|根据类路径中存在的依赖项自动配置Spring应用程序
@ComponentScan|配置用于@Configuration类的组件扫描指令。指定要扫描带注释组件的基包。
@RequestMapping|将Web请求映射到特定的处理程序类或处理程序方法。
@GetMapping|为@RequestMapping(method = RequestMethod.GET)添加标签。处理GET请求。
@PostMapping|为@RequestMapping(method = RequestMethod.POST)添加标签。处理POST请求。
@PutMapping|为@RequestMapping(method = RequestMethod.PUT)添加标签。处理PUT请求。
@DeleteMapping|为@RequestMapping(method = RequestMethod.DELETE)添加标签。处理DELETE请求。
@PatchMapping|为@RequestMapping(method = RequestMethod.PATCH)添加标签。处理PATCH请求。
@RequestParam|将Web请求参数绑定到方法参数。
@PathVariable|将URI模板变量绑定到方法参数。
@ResponseBody|将返回的对象序列化为指定格式（通常是 JSON 格式）并将其作为响应主体返回。
@CrossOrigin|在方法或类上启用跨域资源共享（CORS）
@ExceptionHandler|定义一个方法来处理由请求处理程序方法引发的异常
@ControllerAdvice|允许您处理整个应用程序中的异常，而不仅仅是单个控制器
@RestControllerAdvice|组合@ControllerAdvice和@ResponseBody。REST控制器
@RequestScope|表示 Bean 具有请求作用域
@SessionScope|表示 Bean 是会话作用域的
@ApplicationScope|指示bean是应用程序范围的
@SessionAttributes|用于在会话中存储模型属性
@ModelAttribute|将方法参数或方法返回值绑定到命名的模型属性上
@Async|表示方法应异步执行
@Scheduled|使用 cron 表达式或固定延迟/速率标记要调度的方法
@EnableScheduling|启用对定时任务的支持
@Conditional|根据条件注册bean
@Profile|仅当指定的配置文件处于活动状态时才注册 Bean
@MockBean|在 Spring 应用程序上下文中mock bean（与 Spring Boot 一起用于测试）
@SpyBean|在 Spring 应用程序上下文中创建 spy Bean（与 Spring Boot 一起用于测试）


## 详细说明及用法

### @SpringBootApplication

标记Spring靴子应用程序的主类。结合`@Configuration`、`@EnableAutoConfiguration`和`@ComponentScan`
```java
@SpringBootApplication
public class MySpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySpringBootApplication.class, args);
    }
}
```

### @Component

将类标记为Spring组件，使其成为托管bean

```java
@Component
public class MyComponent {
    // Bean methods and properties
}
```

### @Service

@Component的特殊类型，用于服务层类，通常包含业务逻辑。


```java
@Service
public class MyService {
    // Business logic methods
}
```

### @Repository

数据存储库类的特殊类型@Component。启用异常转换
```java
@Repository
public class MyRepository {
    // Data access methods
}
```

### @Controller

用于处理Web请求的Spring MVC控制器的特殊类型@Component

```java
@Controller
public class MyController {
    @RequestMapping("/hello")
    public String sayHello() {
        return "hello";
    }
}
```
### @RestController

组合@Controller和@ResponseBody。处理Web请求并直接返回JSON/XML响应。


```java
@RestController
public class MyRestController {
    @GetMapping("/hello")
    public String sayHello() {
        return "hello";
    }
}
```

### @Autowired

自动将依赖项注入到字段、setter方法或构造函数中

```java
@Component
public class MyComponent {
    @Autowired
    private MyService myService;

    // Use myService in methods
}
```

### @Value

将属性文件中的值插入字段、setter方法或构造函数参数

```java
@Component
public class MyComponent {
    @Value("${app.name}")
    private String appName;

    // Use appName in methods
}
```
### @Configuration

指示一个类声明一个或多个@Bean方法，并且可以被Spring容器处理以生成bean定义和服务请求

```java
@Configuration
public class MyConfig {
    @Bean
    public MyService myService() {
        return new MyService();
    }
}

```


### @Bean

声明一个方法，该方法返回Spring容器管理的Spring bean

```java
@Configuration
public class MyConfig {
    @Bean
    public MyService myService() {
        return new MyService();
    }
}
```
### @EnableAutoConfiguration

根据类路径中存在的依赖项自动配置Spring应用程序

```java
@SpringBootApplication
public class MySpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySpringBootApplication.class, args);
    }
}

```
> SpringBootApplication是一个组合注解，组合了`@EnableAutoConfiguration`


### @ComponentScan

配置用于`@Configuration`类的组件扫描指令。指定要扫描带注释组件的基包。

```java
@Configuration
@ComponentScan(basePackages = "com.example")
public class MyConfig {
    // Configuration methods
}
```

### @RequestMapping

将Web请求映射到特定的处理程序类或处理程序方法。

```java
@Controller
public class MyController {
    @RequestMapping("/hello")
    public String sayHello() {
        return "hello";
    }
}
```

### @GetMapping

为@RequestMapping(method = RequestMethod.GET)添加标签。处理GET请求。

```java
@RestController
public class MyRestController {
    @GetMapping("/greet")
    public String greet() {
        return "Hello, World!";
    }
}
```

### @PostMapping

为@RequestMapping(method = RequestMethod.POST)添加标签。处理POST请求。

```java
@RestController
public class MyRestController {
    @PostMapping("/create")
    public String create(@RequestBody MyEntity entity) {
        // Process entity
        return "Created";
    }
}
```

### @PutMapping

为@RequestMapping(method = RequestMethod.PUT)添加标签。处理PUT请求。

```java
@RestController
public class MyRestController {
    @PutMapping("/update")
    public String update(@RequestBody MyEntity entity) {
        // Update entity
        return "Updated";
    }
}

```

### @DeleteMapping

为@RequestMapping(method = RequestMethod.DELETE)添加标签。处理DELETE请求。

```java
@RestController
public class MyRestController {
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        // Delete entity by id
        return "Deleted";
    }
}
```

### @PatchMapping

为@RequestMapping(method = RequestMethod.PATCH)添加标签。处理PATCH请求。

```java
@RestController
public class MyRestController {


    @PatchMapping("/patch")
    public String patch(@RequestBody MyEntity entity) {
        // Patch entity
        return "Patched";
    }
}
```

### @RequestParam

将Web请求参数绑定到方法参数。

```java
@RestController
public class MyRestController {
    @GetMapping("/param")
    public String param(@RequestParam String name) {
        return "Hello, " + name;
    }
}
```

### @PathVariable

将URI模板变量绑定到方法参数。

```java

@RestController
public class MyRestController {
    @GetMapping("/path/{id}")
    public String path(@PathVariable Long id) {
        return "ID: " + id;
    }
}
```

### @RequestBody

将HTTP请求的`json body`绑定到方法参数。

```java
@RestController
public class MyRestController {
    @PostMapping("/body")
    public String body(@RequestBody MyEntity entity) {
        return "Received: " + entity;
    }
}
```

### @ResponseBody

将返回的对象序列化为指定格式（通常是 JSON 格式）并将其作为响应主体返回。

```java
@Controller
public class MyController {
    @RequestMapping("/json")
    @ResponseBody
    public String json() {
        return "{\"message\":\"Hello, World!\"}";
    }
}
```

### @CrossOrigin

在方法或类上启用跨域资源共享（CORS）

```java
@RestController
@CrossOrigin(origins = "http://example.com")
public class MyRestController {
    @GetMapping("/cors")
    public String cors() {
        return "CORS Enabled";
    }
}
```

### @ExceptionHandler

定义一个方法来处理由请求处理程序方法引发的异常

```java
@Controller
public class MyController {
    
    @ExceptionHandler(Exception.class)
    public String handleException() {
        return "error";
    }
}
```

> 仅对`MyController`生效

### @ControllerAdvice

允许您处理整个应用程序中的异常，而不仅仅是单个控制器

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleException() {
        return "error";
    }
}
```

> 对所有`controller`生效


### @RestControllerAdvice

组合@ControllerAdvice和@ResponseBody。REST控制器

```java
@RestControllerAdvice
public class GlobalRestExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleException() {
        return "{\"error\":\"An error occurred\"}";
    }
}
```

### @RequestScope

@RequestScope 注解在 Spring 框架中用于定义 Bean 的作用域，使得每个 HTTP 请求都有实例化的独立 Bean。这意味着在同一个请求过程中，一个带有 @RequestScope 的 Bean 实例会被共享，但在不同的请求中会创建新的实例

```java
@Component
@RequestScope
public class MyRequestScopedBean {
    // Bean methods and properties
}
```

### @SessionScope

`@SessionScope` 注解在 `Spring` 框架中用于定义 Bean 的作用域，使得每个 HTTP 会话都有实例化的独立 Bean。这意味着在同一个会话过程中，一个带有 `@SessionScope` 的 Bean 实例会被共享，但在不同的会话中会创建新的实例

```java
@Component
@SessionScope
public class MySessionScopedBean {
    // Bean methods and properties
}
```
### @ApplicationScope

`@ApplicationScope` 注解在 `Spring` 框架中用于定义 Bean 的作用域，使得每个应用程序上下文都有实例化的独立 Bean。这意味着在同一个应用程序上下文中，一个带有 `@ApplicationScope` 的 Bean 实例会被共享，但在不同的应用程序上下文中会创建新的实例

```java
@Component
@ApplicationScope
public class MyApplicationScopedBean {
    // Bean methods and properties
}
```

### @SessionAttributes

`@SessionAttributes` 注解用于在会话中存储模型属性。这些属性在多个请求之间共享，并且在会话结束时删除

```java
@Controller
@SessionAttributes("user")
public class MyController {
    @ModelAttribute("user")
    public User createUser() {
        return new User();
    }
}
```

### @ModelAttribute
    
`@ModelAttribute` 注解用于将方法参数或方法返回值绑定到命名的模型属性上
    
```java
@Controller
public class MyController {
    @ModelAttribute("attribute")
    public String addAttribute() {
        return "value";
    }
}
```

### @Async
    
`@Async` 注解表示方法应异步执行

```java
@Service
public class MyService {
    @Async
    public void asyncMethod() {
        // Async method logic
    }
}
```

### @Scheduled

`@Scheduled` 注解使用 cron 表达式或固定延迟/速率标记要调度的方法

```java
@Component
public class MyScheduledTask {
    @Scheduled(cron = "0 0 * * * ?")
    public void scheduledMethod() {
        // Scheduled task logic
    }
}
```

### @EnableScheduling
    
`@EnableScheduling` 注解启用对定时任务的支持

```java
@Configuration
@EnableScheduling
public class MyConfig {
    // Configuration methods
}
```
    
### @Conditional
    
`@Conditional` 注解根据条件注册 bean

```java
@Configuration
public class MyConfig {
    @Bean
    @Conditional(MyCondition.class)
    public MyService myService() {
        return new MyService();
    }
}
```

### @Profile

`@Profile` 注解仅当指定的配置文件处于活动状态时才注册 Bean

```java
@Configuration
@Profile("dev")
public class DevConfig {
    @Bean
    public MyService myService() {
        return new MyService();
    }
}
```

### @MockBean
    
`@MockBean` 注解在 Spring 应用程序上下文中 mock bean（与 Spring Boot 一起用于测试）


```java
@SpringBootTest
public class MyTest {
    
    @MockBean
    private MyService myService;

    @Test
    public void test() {
        // Test logic
    }
}
```

### @SpyBean

`@SpyBean` 注解在 Spring 应用程序上下文中创建 spy Bean（与 Spring Boot 一起用于测试）

```java
@SpringBootTest
public class MyTest {
    @SpyBean
    private MyService myService;

    @Test
    public void test() {
        // Test logic
    }
}
```

## 参考

上面的内容绝大部分来源于 https://www.rameshfadatare.com/cheat-sheet/spring-and-spring-boot-annotations-cheat-sheet/

感谢`Ramesh Fadatare`的整理分享