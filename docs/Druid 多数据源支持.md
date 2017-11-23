# Druid 多数据源支持

1.0.2 版本新增了支持多数据源的 DruidDataSource2，Starter 可以自动将 `spring.datasource` 和 `spring.datasource.druid` 开头的属性注入到每一个数据源，用户只需要声明每个数据源单独的属性即可。

1.1.2 版本则更进一步，改进了多数据源的注册方式，无需任何代码，由 Starter 自动发现配置并注册到 ApplicationContext，旨在简化开发人员的劳动力。


## 使用方式

编辑 application 配置文件，Starter 会将 `spring.datasource.druid.data-sources` 开头的的属性注入到 Map<String, DruidDataSource2> 中，并根据该 Map 构建数据源。
    
```yaml
spring:
  autoconfigure:
    ## 多数据源环境下必须排除掉 DataSourceAutoConfiguration，否则会导致循环依赖报错
    exclude:
      - org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
  datasource:
    ## 以 'spring.datasource' 和 'spring.datasource.druid' 开头的属性会作为公共配置，注入到每一个数据源
    driver-class-name: org.h2.Driver
    username: root
    password: 123456
    druid:
      username: root
      password: 123456
      ## 多数据源的标识，若该属性存在则为多数据源环境，不存在则为单数据源环境
      data-sources:
        ### master 数据源的配置，以下为 master 数据源独有的配置
        master:
          url: jdbc:h2:file:./master
        ### slave 数据源的配置，以下为 slave 数据源独有的配置
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

### 公共配置
多数据源其实很好实现，编写 N 个数据源的配置，并且声明 N 个 Bean 即可，但带来的一个问题是各数据源之间存在大量相同的配置，每个数据源全部声明一遍既繁琐又无用，为了解决这个问题，Starter 中新增了两个类：DruidDataSource2 和 DruidDataSource2Support。

其中 DruidDataSource2Support 是个抽象类，继承自 com.alibaba.druid.pool.DruidDataSource，主要作用是自动注入 `spring.datasource` 和 `spring.datasource.druid` 开头的属性，并作为 parent bean 为各个数据源实例提供公共配置。

DruidDataSource2 继承自 DruidDataSource2Support，内部没有任何代码，目的是继承 DruidDataSource2Support 中的已注入的配置，同时可以注入各数据源单独的配置，如遇到相同的属性会覆盖父类的同名属性。

也就是说，以下两种方式是等价的：

方式一：
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

方式二：
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

### 自动配置

Starter 判断是否多数据源的条件是 application 文件中是否存在 `spring.datasource.druid.data-sources` 属性，若存在即为多数据源环境，不存在则为单数据源环境。

多数据源环境下，Starter 会将 `spring.datasource.druid.data-sources` 开头的属性注入到 Map<String, DruidDataSource2> 中，然后遍历该 Map，并通过构造 BeanDefinition 的方式来构造各个数据源，最后通过 PropertySourcesBinder 将各数据源单独的属性注入到各数据源。


## 演示
将 [druid-spring-boot-mybatis-example](https://github.com/drtrang/druid-spring-boot/blob/master/druid-spring-boot-example/druid-spring-boot-mybatis-example/src/main/resources/application.yml) 的 `spring.profiles.active` 配置改为 `dynamic` 即可。
