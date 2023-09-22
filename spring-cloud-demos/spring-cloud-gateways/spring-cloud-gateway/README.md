## spring cloud gateway

## 配置

- 路由
```yaml
spring:
  cloud:
    gateway:
      routes: 
        - id: order-service
          predicates:
            - Path=/ms/order-service/**
          filters:
            - StripPrefix=2
          uri: lb://order-service
        - id: pay-service
          predicates:
            - Path=/ms/pay-service/**
          filters:
            - StripPrefix=1
          uri: http://localhost:9999/pay-service
```

- httpclient配置

```yaml
spring:
  cloud:
    gateway:
      httpclient:
        response-timeout: 300s
        pool:
          type: fixed
          max-idle-time: 5000
          max-connections: 200 
          acquire-timeout: 45000
```
- max-idle-time  最大空闲时间 毫秒
- max-connections 连接池中允许的最大连接数
- acquire-timeout 获取连接最大等待时间 默认45s(45000)
- type 连接池类型，有三种
  - ELASTIC（弹性连接池）：弹性连接池会根据当前的负载情况自动调整连接池的大小。它会根据请求的压力动态地增加或减少连接数，以适应负载的变化。这种连接池适合于负载波动较大的场景，能够自动调整连接数以应对高峰期和低谷期的负载变化。
  - FIXED（固定连接池）：固定连接池会在启动时创建固定数量的连接，并保持不变。这种连接池适合于负载相对稳定的场景，连接数不会随着负载的变化而调整。固定连接池可以提供一定的稳定性和可控性，适用于对连接数有明确要求的情况。
  - DISABLED（禁用连接池）：禁用连接池意味着每个请求都会创建一个新的连接，而不使用连接池。这种模式适用于对连接池不感兴趣或者希望每个请求都使用一个独立的连接的场景。但需要注意，禁用连接池可能会导致资源浪费和性能下降，因为连接的创建和销毁开销较大。

## sentinel限流

1. 引入依赖
```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
  </dependency>
  <dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-transport-simple-http</artifactId>
    <version>1.8.6</version>
  </dependency>

  <dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-spring-cloud-gateway-adapter</artifactId>
    <version>1.8.6</version>
  </dependency>
</dependencies>
```

启动参数
```yaml
-Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=xiaozou-gateway
```

需要注意这些启动参数不支持yaml配置使用，如果需要使用yaml配置，需要使用
```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```