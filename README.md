# Druid Spring Boot Starter

[![Build Status](https://api.travis-ci.org/drtrang/druid-spring-boot.svg?branch=master)](https://www.travis-ci.org/drtrang/druid-spring-boot)
[![Coverage Status](https://coveralls.io/repos/github/drtrang/druid-spring-boot/badge.svg?branch=master)](https://coveralls.io/github/drtrang/druid-spring-boot?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.drtrang/druid-spring-boot/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.drtrang/druid-spring-boot)
[![License](http://img.shields.io/badge/license-apache%202-brightgreen.svg)](https://github.com/drtrang/druid-spring-boot/blob/master/LICENSE)

Druid Spring Boot Starter 将帮助你在 Spring Boot 中使用 Druid。

## Maven 依赖
```xml
<dependency>
    <groupId>com.github.drtrang</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

## 配置
Druid Spring Boot Starter 添加了 Druid 几乎所有的特性，如 WallFilter、ConfigFilter、StatViewServlet 
等等，且仅需少量配置即可开启。

在引入依赖的情况下，只需以下配置即可使用 Druid:
```
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:file:./samples
    username: root
    password: 123456
```



使用 DruidDataSource 的默认值

## 作者信息
QQ：349096849
Email：donghao.l@hotmail.com