## ConditionalOnBean 问题

测试demo在 com.spring.boot.base.conditionalOnBeanTest

 spring ioc优先解析`@Component`，`@Service`，`@Controller`注解的类.其次是`@Configuration`


 如果使用`@ConditionalOnBean`的两个Bean都在`@Configuration`,那么无法保证java顺序导致某些bean明明存储也不会加载另一个bean
 
可以使用`ConditionalOnBean`替代。或者通过`AutoConfigureBefore`、`AutoConfigureAfter`  `AutoConfigureOrder` 设置加载顺序

详情参数[博客](https://blog.csdn.net/forezp/article/details/84313907)

## applicationName 获取问题

使用@Value("${spring.application.name}")获取不到值，需要使用@Value("${spring.application.name:}")，否则会报错

也可以使用`@Autowired Environment env`获取