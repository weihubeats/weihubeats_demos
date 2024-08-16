# spring boot 获取请求头

1. 使用@RequestHeader注解直接获取指定的请求头参数

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeaderController {

    @GetMapping("/header")
    public String getHeader(@RequestHeader("User-Agent") String userAgent) {
        return "User-Agent: " + userAgent;
    }

    @GetMapping("/headers")
    public String getAllHeaders(@RequestHeader Map<String, String> headers) {
        return "Headers: " + headers.toString();
    }

    @GetMapping("/headersHttp")
    public String getAllHeadersHttp(@RequestHeader HttpHeaders httpHeaders) {
        return "Headers: " + httpHeaders.toString();
    }
}
```

2.  使用HttpServletRequest对象

```java
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeaderController {

    @GetMapping("/headerRequest")
    public String getHeaderByRequest(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return "User-Agent: " + userAgent;
    }
}
```