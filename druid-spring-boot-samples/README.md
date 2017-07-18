# Druid Spring Boot Samples

1.0.2 版本中新增多数据源支持，开启该特性需要手动声明各个 DataSource，并指定注入的前缀（`spring.datasource.druid.${name}`）即可，
其中 `${name}` 可自定义。

配置参考 `com.github.trang.druid.config.SpringDataSourceConfig`。

注意：
1. 手动声明数据源的类型为 `com.github.trang.druid.datasource.DruidMultiDataSource`
2. 多数据源场景会导致 `javax.sql.DataSource` 和 `org.springframework.boot.autoconfigure.jdbc.DataSourceInitializer` 循环依赖，
所以需要手动排除 `org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration`，以下方式可任选其一：
    1. 在 `@SpringBootApplication` 注解中排除
    ```
    @SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
    ```
    2. 在 `application.yml` 中排除
    ```yaml
    spring:
      autoconfigure:
        exclude:
          - org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
    ```


其中以 `spring.datasource.druid` 为前缀的配置可以作为公共参数注入到子数据源

```yaml
spring:
  datasource:
    druid:
      driver-class-name: org.h2.Driver
      initial-size: 1
      min-idle: 1
      max-active: 10
      one:
        # 继承 spring.datasource.druid 前缀的所有配置，名称相同的配置会覆盖
        url: jdbc:h2:file:./one
        username: root
        password: 123456
      two:
        url: jdbc:h2:file:./two
        username: root
        password: 123456
```

## 示例
将 [druid-spring-boot-samples](https://github.com/drtrang/druid-spring-boot/tree/master/druid-spring-boot-samples) 中
[application.yml](https://github.com/drtrang/druid-spring-boot/blob/master/druid-spring-boot-samples/src/main/resources/application.yml)
的 `spring.profiles.active` 配置改为 `dynamic` 即可。