# Changelog

### 1.1.3
1. 修复多数据源环境下的注册失败问题

### 1.1.2
1. 改进多数据源的声明方式，由 Starter 自动发现配置并注册到 ApplicationContext

### 1.1.1
1. 新增 `statement-sql-format-option` 的代码提示

### 1.1.0
1. 新增 `druid-spring-boot-actuator-starter` 模块
3. 更改包名称为 `com.github.trang.druid.autoconfigure`
4. 更改数据源名称为 `DruidDataSource2`
2. 优化 Auto-configure Bean 的加载条件
5. 优化 Maven 依赖

### 1.0.3
1. 新增 ConfigFilter 的自动配置，替换 Druid 默认的 `connectionProperties` 方式
2. 修复 `spring.datasource.schema` 会执行两次的 bug

### 1.0.2
1. 新增 WallConfig 的自动配置
2. 新增多数据源支持

### 1.0.1 
1. 新增一部分 application.yml 的自动提示，如 `enabled`、`db-type` 等
2. 统一配置文件位置

### 1.0.0
1. 提供基于 Druid 的 Spring Boot Starter