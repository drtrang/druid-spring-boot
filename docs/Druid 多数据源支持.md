# Druid 多数据源支持

1.0.2 版本新增支持多数据源的 **DruidDataSource2**，开启该特性需要手动声明数据源，并指定注入的前缀 `spring.datasource.druid.${name}`，其中 `${name}` 可根据实际情况自定义。


## 使用方式

1. 声明各个 DataSource，类型为 `com.github.trang.druid.autoconfigure.datasource.DruidDataSource2`。
    ```java
    @Bean
    @ConfigurationProperties("spring.datasource.druid.one")
    public DruidDataSource firstDataSource() {
        return new DruidDataSource2();
    }
    ```

2. 手动排除 `org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration`，以下方式任选其一：
    1. 在 `@SpringBootApplication` 注解中排除
        ```java
        @SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
        ```
    2. 在 `application.yml` 中排除
        ```yaml
        spring:
          autoconfigure:
            exclude:
              - org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
        ```

## 原理

DruidDataSourceSupport 类存在的目的是为了注入 `spring.datasource.druid` 的配置。
基于 Spring4 的特性，DruidDataSource2 在继承 DruidDataSourceSupport 的同时，也会继承这些配置，由此解决多数据源场景下相同配置重复定义的问题。

也就是说，以下两种方式是等价的：
```yaml
spring:
  datasource:
    druid:
      min-evictable-idle-time-millis: 1800000
      max-evictable-idle-time-millis: 25200000
      one:
        name: one
      two:
        name: two
---
spring:
  datasource:
    druid:
      one:
        name: one
        min-evictable-idle-time-millis: 1800000
        max-evictable-idle-time-millis: 25200000
      two:
        name: two
        min-evictable-idle-time-millis: 1800000
        max-evictable-idle-time-millis: 25200000
```

若子数据源有相同的配置时，则会覆盖掉父数据源的值：
```yaml
spring:
  datasource:
    druid:
      max-active: 20
      one:
        name: one
        max-active: 50
      two:
        name: two
```

## 演示
将 [druid-spring-boot-samples](https://github.com/drtrang/druid-spring-boot/tree/master/druid-spring-boot-samples) 中 [application.yml](https://github.com/drtrang/druid-spring-boot/blob/master/druid-spring-boot-samples/src/main/resources/application.yml) 的 `spring.profiles.active` 配置改为 `dynamic` 即可。