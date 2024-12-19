## 传统的参数校验


相信现在的大部分`web`项目进行参数校验都是通过如下方式

1. 添加依赖

```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
```

2. 在 controller进行参数校验

- ValidationController

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ValidationController {

    private final XiaoZouService xiaoZouService;

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody XiaoZouDTO xiaoZouDTO) {
        xiaoZouService.createUser(xiaoZouDTO);
        return ResponseEntity.ok("用户创建成功");
    }
}
```

- XiaoZouDTO

```java
@Data
public class XiaoZouDTO {

    @NotNull(message = "用户ID不能为空")
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Length(min = 2, max = 20, message = "用户名长度必须在2-20之间")
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}
```

实际的业务逻辑处理被封装在`xiaoZouService`

### 异常处理器

我们再随便加一个全局异常处理器

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.append(fieldName).append(": ").append(errorMessage).append("; ");
        });
        return ResponseEntity.badRequest().body(errors.toString());
    }
}
```

### 测试

然后我们进行接口请求

```curl
POST http://localhost:8091/api/xiao-zou
Accept: application/json
Content-Type: application/json

{
  "name": "xiao-zou",
  "age": 18
}
```

参数我们随便乱传，就会出现如下错误信息提示

```
email: 邮箱不能为空; id: 用户ID不能为空; username: 用户名不能为空; 
```

结果看似满足我们的预期。

但是目前我们会注意到一个问题

1. 我们的参数实际使用是在`service`，但是我们的参数校验是在`controller`
2. 如果我们存在自定义aop切面进行参数修改，aop的切面执行顺序在参数校验之后，那么我们的参数校验就失效了
3. 我们的`service`方法并不单单仅在`controller`中调用，我们的`service`方法可能会在其他地方调用，比如`job`、`mq`、`RPC`等等，这种情况下我们的参数校验就失效了

我们来实际举例说明


## AOP切面修改参数导致的参数校验失败

假设我们的的邮箱为了数据安全之类的业务背景，统一通过请求头获取

那么我们可能定义如下一个切面

```java
@Aspect
@Slf4j
@RequiredArgsConstructor
public class XiaoZouHttpAspect {

    @Pointcut("@annotation(com.spring.boot.base.annotation.XiaoZouResponse)")
    public void controllerMethodAspect() {

    }
    
    @GetMapping
    @Before("controllerMethodAspect()")
    public void doBefore(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        if (Objects.isNull(args) || args.length == 0) {
            return;
        }
        for (Object argObj : args) {
            if (argObj instanceof XiaoZouDTO) {
                XiaoZouDTO xiaoZouDTO = (XiaoZouDTO) argObj;
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest httpServletRequest = requestAttributes.getRequest();
                xiaoZouDTO.setEmail(httpServletRequest.getHeader("x-xiaozou-email"));
            }
        }
        
    }
    
}
```

此时如果请求头里面没有`x-xiaozou-email`，但是我们的body里面传了`email`。

这时候的参数校验也会失败，失败步骤如下

1. `XiaoZouDTO`接收到body的`email`参数进行参数校验通过了参数校验
2. `XiaoZouHttpAspect`切面修改了`email`参数，但是这个修改是在参数校验之后的，`email`为空进入到`service`层，所以参数校验失败


## dubbo调用参数校验失效

`dubbo例`的测试用就很简单了。我们的`service`可能既给`controller`调用，也可能给`dubbo`调用

```java
@DubboService
public class XiaoZouServiceImpl implements XiaoZouService {

    @Override
    public String createUser(XiaoZouDTO xiaoZouDTO) {
        return name;
    }
    
}
```

从`dubbo`过来的请求不会经过我们在`controller`的参数校验，所以参数校验失效

这里的业务逻辑处理就会因为必要的参数为空出现不符合我们预期的业务逻辑


## MQ、xxl-job参数校验失败

`MQ`、`xxl-job`等等的调用也是一样的，我们的参数校验只会在`controller`中生效，其他地方调用就会失效


## 最佳实践(解决方式)


很明显最佳的参数校验并不是在`controller`中进行，而是在`service`中进行

因为我们的参数实际是给`service`使用的，我们的很多请求也会绕过`controller`导致`service`参数校验失效


所以最佳方式应该是在接口层`service`进行参数校验

如果我们想要对`XiaoZouService`进行参数校验，我们应该使用如下方式

```java
@Validated
public interface XiaoZouService {

    boolean createUser(@Valid XiaoZouDTO xiaoZouDTO);
}


```

1. 我们在`service`接口上加上`@Validated`注解
2. 在`service`方法上加上`@Valid`注解


这样我们的参数校验就会在`service`层生效了。

不管请求是从`controller`、`dubbo`、`mq`、`job`等等过来的，我们的参数校验都会生效。

就不会出现因为参数校验失效导致的业务逻辑出现问题了


## 总结

目前行业内大部分的教程和规范都是在`controller`中进行参数校验

导致大家都在`controller`中进行参数校验。实际这个做法是非常不合理和容易出问题的

如果我们在指定项目规范的时候，我们应该规定参数校验应该在`service`层进行，而不是在`controller`层进行