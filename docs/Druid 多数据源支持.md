# Druid 多数据源支持

1.0.2 版本新增了支持多数据源的 **DruidDataSource2**，可以将 `spring.datasource` 和 `spring.datasource.druid` 开头的配置注入到数据源，解决各数据源之间的相同配置问题。
1.1.2 版本改进了多数据源的注册方式，由 Starter 自动发现配置并注册到 Spring Context，用户只需在配置文件中声明即可。


## 使用方式

1. 编辑配置文件，Starter 会将 `spring.datasource.druid.data-sources` 开头的的属性注入到 Map<String, DruidDataSource2> 中，从而构建多数据源。
    ```yaml
    spring:
      autoconfigure:
        exclude:
          - org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
      datasource:
        driver-class-name: org.h2.Driver
        username: root
        password: 123456
        druid:
          # 多数据源的标识，若该属性存在则为多数据源环境
          data-sources:
            ## master 数据源的配置，以下为 master 数据源独有的配置
            master:
              url: jdbc:h2:file:./master
            ## slave 数据源的配置，以下为 slave 数据源独有的配置
            slave:
              url: jdbc:h2:file:./slave
    ```

注： 排除 `org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration` 有两种方式，以下方式任选其一即可：
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

DruidDataSource2Support 类存在的目的是为了注入 `spring.datasource` 和 `spring.datasource.druid` 的配置。
基于 Spring4 的特性，DruidDataSource2 在继承 DruidDataSource2Support 的同时，也会继承这些配置，由此解决多数据源场景下相同配置重复定义的问题。

也就是说，以下两种方式是等价的：
```yaml
spring:
  datasource:
    druid:
      driver-class-name: org.h2.Driver
      username: root
      password: 123456
      data-sources:
        master:
          url: jdbc:h2:file:./master
        slave:
          url: jdbc:h2:file:./slave
```

```yaml
spring:
  datasource:
    druid:
      master:
        driver-class-name: org.h2.Driver
        username: root
        password: 123456
        url: jdbc:h2:file:./master
      slave:
        driver-class-name: org.h2.Driver
        username: root
        password: 123456
        url: jdbc:h2:file:./slave
```

## 演示
将 [druid-spring-boot-example](https://github.com/drtrang/druid-spring-boot/blob/master/druid-spring-boot-example/druid-spring-boot-mybatis-example/src/main/resources/application.yml) 的 `spring.profiles.active` 配置改为 `dynamic` 即可。