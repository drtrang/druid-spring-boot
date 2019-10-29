# Druid Spring Boot Starter

[![Build Status](https://img.shields.io/travis/drtrang/druid-spring-boot/boot2.svg?style=flat-square)](https://www.travis-ci.org/drtrang/druid-spring-boot)
[![Coverage Status](https://img.shields.io/coveralls/drtrang/druid-spring-boot/boot2.svg?style=flat-square)](https://coveralls.io/github/drtrang/druid-spring-boot?branch=boot2)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.drtrang/druid-spring-boot2.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.github.drtrang/druid-spring-boot2)
[![GitHub Release](https://img.shields.io/github/release/drtrang/druid-spring-boot.svg?style=flat-square)](https://github.com/drtrang/druid-spring-boot/releases)
[![License](http://img.shields.io/badge/license-apache%202-blue.svg?style=flat-square)](https://github.com/drtrang/druid-spring-boot/blob/boot2/LICENSE)

Druid Spring Boot Starter 将帮助你在 Spring Boot 中使用 Druid。


## 依赖
```xml
<!-- spring boot 1.x -->
<dependency>
    <groupId>com.github.drtrang</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.10</version>
</dependency>
<dependency>
    <groupId>com.github.drtrang</groupId>
    <artifactId>druid-spring-boot-actuator-starter</artifactId>
    <version>1.1.10</version>
</dependency>

<!-- spring boot 2.x -->
<dependency>
    <groupId>com.github.drtrang</groupId>
    <artifactId>druid-spring-boot2-starter</artifactId>
    <version>1.1.10</version>
</dependency>
<dependency>
    <groupId>com.github.drtrang</groupId>
    <artifactId>druid-spring-boot2-actuator-starter</artifactId>
    <version>1.1.10</version>
</dependency>
```


## 意见征集
[意见征集](https://github.com/drtrang/druid-spring-boot/issues/10)


## NEW !
1. 基于 Spring Boot 2 开发的全新 starter，与之前功能完全一致
2. 改进多数据源的声明方式，由 Starter 自动发现配置并注册到 ApplicationContext，详情请查看 [Druid 多数据源支持.md](https://github.com/drtrang/druid-spring-boot/tree/boot2/docs/Druid%20%E5%A4%9A%E6%95%B0%E6%8D%AE%E6%BA%90%E6%94%AF%E6%8C%81.md)
3. 新增全配置说明 [druid.yml](https://github.com/drtrang/druid-spring-boot/blob/boot2/druid-spring-boot2-example/druid-spring-boot2-mybatis-example/src/main/resources/druid.yml)


## 配置
### 简单配置
在引入依赖的情况下，只需如下配置即可使用 Druid：

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:example
    username: root
    password: 123456
```

### Druid 连接池
Druid Spring Boot Starter 会将以 `spring.datasource.druid` 为前缀的配置注入到 DruidDataSource，且 DruidDataSource 中的所有参数均可自定义。

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:example
    username: root
    password: 123456
    druid:
      max-wait: 30000
      query-timeout: 10
      validation-query: SELECT 'x'
      use-global-data-source-stat: true
```

### Druid 高级特性
Druid Spring Boot Starter 添加了 Druid 的大部分特性，如 StatFilter、WallFilter、ConfigFilter、WebStatFilter 等，其中 StatFilter 默认打开，其它特性默认关闭，需要手动开启。

同样，每个特性的参数均可自定义，具体配置可以用 IDE 的自动提示功能或者阅读 Druid 的 [Wiki](https://github.com/alibaba/druid/wiki/%E9%A6%96%E9%A1%B5) 查看。

```yaml
spring:
  datasource:
    druid:
      # 开启 StatFilter，默认开启，可通过 'enabled: false' 关闭
      stat:
        enabled: true
        log-slow-sql: true
        slow-sql-millis: 1000
      # 开启 Slf4jFilter
      slf4j:
        enabled: true
        data-source-log-enabled: false
        connection-log-enabled: false
        statement-log-enabled: false
        result-set-log-enabled: false
      # 开启 WallFilter
      wall:
        enabled: true
        log-violation: true
        throw-exception: false
        ## WallConfig 配置
        config:
          delete-where-none-check: true
      # 开启 ConfigFilter
      config:
        enabled: true
      # 开启 Web 监控
      web-stat:
        enabled: true
      # 开启 Aop 监控
      aop-stat:
        enabled: true
      # 开启监控页面
      stat-view-servlet:
        enabled: true
```

### 多数据源
1.1.2 版本改进了多数据源的声明方式，由 Starter 自动发现配置并注册到 ApplicationContext，详情请查看 [Druid 多数据源支持.md](https://github.com/drtrang/druid-spring-boot/tree/boot2/docs/Druid%20%E5%A4%9A%E6%95%B0%E6%8D%AE%E6%BA%90%E6%94%AF%E6%8C%81.md)。

```yaml
spring:
  datasource:
    druid:
      data-sources:
        master:
          url: jdbc:h2:file:./master
        slave:
          url: jdbc:h2:file:./slave
```

### 配置示例
[application.yml](https://github.com/drtrang/druid-spring-boot/blob/boot2/druid-spring-boot2-example/druid-spring-boot2-mybatis-example/src/main/resources/application.yml)

### 全配置说明
[druid.yml](https://github.com/drtrang/druid-spring-boot/blob/boot2/druid-spring-boot2-example/druid-spring-boot2-mybatis-example/src/main/resources/druid.yml)


## 自动提示
Druid Spring Boot Starter 基于 `spring-boot-configuration-processor` 模块，支持 IDE 的自动提示。

自定义参数：<br>
![druid-configuration](https://user-images.githubusercontent.com/13851701/28149522-c1a3fc96-67c0-11e7-8ea7-630a8b3e5bfb.png)

参数说明：
![enabled](https://user-images.githubusercontent.com/13851701/28149525-d08955bc-67c0-11e7-916c-c8c5acd30b4a.png)

参数枚举值：
![db-type](https://user-images.githubusercontent.com/13851701/28148904-3bb9b07a-67bc-11e7-9912-c7043c2d7de7.png)


## 演示
[druid-spring-boot2-example](https://github.com/drtrang/druid-spring-boot/tree/boot2/druid-spring-boot2-example) 中演示了 Druid Spring Boot Starter 的使用方式，可以作为参考。 


## Change Log
[Release Notes](https://github.com/drtrang/druid-spring-boot/releases)


## TODO
任何意见和建议可以提 [ISSUE](https://github.com/drtrang/druid-spring-boot/issues)，我会酌情加到 [TODO List](https://github.com/drtrang/druid-spring-boot/blob/boot2/TODO.md)，一般情况一周内迭代完毕。


## About Me
QQ：349096849<br>
Email：donghao.l@hotmail.com<br>
Blog：[Trang's Blog](http://blog.trang.space)