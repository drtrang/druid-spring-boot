# Druid Spring Boot Starter

[![Build Status](https://img.shields.io/travis/drtrang/druid-spring-boot/master.svg?style=flat-square)](https://www.travis-ci.org/drtrang/druid-spring-boot)
[![Coverage Status](https://img.shields.io/coveralls/drtrang/druid-spring-boot/master.svg?style=flat-square)](https://coveralls.io/github/drtrang/druid-spring-boot?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.drtrang/druid-spring-boot.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.github.drtrang/druid-spring-boot)
[![GitHub Release](https://img.shields.io/github/release/drtrang/druid-spring-boot.svg?style=flat-square)](https://github.com/drtrang/druid-spring-boot/releases)
[![License](http://img.shields.io/badge/license-apache%202-blue.svg?style=flat-square)](https://github.com/drtrang/druid-spring-boot/blob/master/LICENSE)

Druid Spring Boot Starter 将帮助你在 Spring Boot 中使用 Druid。


## 依赖
```xml
<dependency>
    <groupId>com.github.drtrang</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.2</version>
</dependency>
```

## NEW !
1. 改进多数据源的声明方式，由 Starter 自动发现配置并注册到 Spring Context，详情请查看 [Druid 多数据源支持.md](https://github.com/drtrang/druid-spring-boot/tree/master/docs/Druid%20%E5%A4%9A%E6%95%B0%E6%8D%AE%E6%BA%90%E6%94%AF%E6%8C%81.md)。
1. 新增全配置说明 [druid.yml](https://github.com/drtrang/druid-spring-boot/blob/master/druid-spring-boot-example/druid-spring-boot-mybatis-example/src/main/resources/druid.yml)
2. 新增 `druid-spring-boot-actuator-starter`


## 配置
### 简单配置
在引入依赖的情况下，只需如下配置即可使用 Druid：

```yaml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:file:./samples
    username: root
    password: 123456
```

### Druid 连接池
Druid Spring Boot Starter 会将以 `spring.datasource.druid` 为前缀的配置注入到 DruidDataSource，且 DruidDataSource 中的所有参数均可自定义。

```yaml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:file:./samples
    username: root
    password: 123456
    druid:
      initial-size: 1
      min-idle: 1
      max-active: 10
      validation-query: SELECT 1
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
      pool-prepared-statements: true
      max-open-prepared-statements: 20
      use-global-data-source-stat: true
```

### Druid 高级特性
Druid Spring Boot Starter 添加了 Druid 的大部分特性，如 StatFilter、WallFilter、ConfigFilter、WebStatFilter 等，其中 StatFilter 默认打开，其它特性默认关闭，需要手动开启。

同样，每个特性的参数均可自定义，具体配置可以用 IDE 的自动提示功能或者阅读 Druid 的 [Wiki](https://github.com/alibaba/druid/wiki/%E9%A6%96%E9%A1%B5) 查看。

```yaml
spring:
  datasource:
    druid:
      slf4j:
        # 开启 Slf4jFilter
        enabled: true
      wall:
        # 开启 WallFilter
        enabled: true
        config:
          ## WallConfig 配置
          select-all-column-allow: false
      config:
        # 开启 ConfigFilter
        enabled: true
      web-stat:
        # 开启 Web 监控
        enabled: true
      aop-stat:
        # 开启 Aop 监控
        enabled: true
      stat-view-servlet:
        # 开启监控页面
        enabled: true
```

### 多数据源
1.1.2 版本改进了多数据源的声明方式，由 Starter 自动发现配置并注册到 Spring Context，详情请查看 [Druid 多数据源支持.md](https://github.com/drtrang/druid-spring-boot/tree/master/docs/Druid%20%E5%A4%9A%E6%95%B0%E6%8D%AE%E6%BA%90%E6%94%AF%E6%8C%81.md)。

```yaml
spring:
  datasource:
    druid:
      data-sources:
        first:
          url: jdbc:h2:file:./first
        second:
          url: jdbc:h2:file:./second
```

### 配置示例
[application.yml](https://github.com/drtrang/druid-spring-boot/blob/master/druid-spring-boot-example/druid-spring-boot-mybatis-example/src/main/resources/application.yml)

### 全配置说明
[druid.yml](https://github.com/drtrang/druid-spring-boot/blob/master/druid-spring-boot-example/druid-spring-boot-mybatis-example/src/main/resources/druid.yml)


## 自动提示
Druid Spring Boot Starter 基于 `spring-boot-configuration-processor` 模块，支持 IDE 的自动提示。

自定义参数：<br>
![druid-configuration](https://user-images.githubusercontent.com/13851701/28149522-c1a3fc96-67c0-11e7-8ea7-630a8b3e5bfb.png)

参数说明：
![enabled](https://user-images.githubusercontent.com/13851701/28149525-d08955bc-67c0-11e7-916c-c8c5acd30b4a.png)

参数枚举值：
![db-type](https://user-images.githubusercontent.com/13851701/28148904-3bb9b07a-67bc-11e7-9912-c7043c2d7de7.png)


## 演示
[druid-spring-boot-example](https://github.com/drtrang/druid-spring-boot/tree/master/druid-spring-boot-example) 演示了 Druid Spring Boot Starter 的使用方式，可以作为参考。 


## Change Log
[Release Notes](https://github.com/drtrang/druid-spring-boot/releases)


## TODO
任何意见和建议可以提 [ISSUE](https://github.com/drtrang/druid-spring-boot/issues)，我会酌情加到 [TODO List](https://github.com/drtrang/druid-spring-boot/blob/master/TODO.md)，一般情况一周内迭代完毕。


## About Me
QQ：349096849<br>
Email：donghao.l@hotmail.com<br>
Blog：[Trang's Blog](http://blog.trang.space)