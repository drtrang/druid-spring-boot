# Druid Spring Boot Example

Druid Spring Boot Example 项目演示了 Druid Spring Boot Starter 的使用方式。将项目导入 IDE 后，直接运行 `com.github.trang.druid.DruidSpringBootStarterApplication#main()` 即可，无需依赖其它环境。

项目默认使用单数据源，将 [application.yml](https://github.com/drtrang/druid-spring-boot/blob/master/druid-spring-boot-example/druid-spring-boot-mybatis-example/src/main/resources/application.yml) 的 `spring.profiles.active` 配置改为 `dynamic` 即可切换为多数据源演示。