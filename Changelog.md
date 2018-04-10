# Changelog

## 1.1.9
### Enhancements
1. 升级 druid 到 1.1.9
2. 升级 Spring Boot 版本到 2.0.1.RELEASE

## 1.1.8
### Enhancements
1. 升级 druid 到 1.1.8
2. 升级 Spring Boot 版本到 2.0.0.RELEASE
2. 精简代码，完善测试用例，完善最佳实践

## 1.1.7
### Enhancements
1. 升级 druid 到 1.1.7，以后版本号与 druid 保持一致

## 1.1.6
### Enhancements
1. 新增基于 JPA 的代码示例
2. 改进 DruidDataSourceInitializer，支持多数据源的 SQL 初始化
3. 改进自动提示，新增大量精准提示，详情见 [additional-spring-configuration-metadata.json](https://github.com/drtrang/druid-spring-boot/blob/master/druid-spring-boot2/druid-spring-boot2-autoconfigure/src/main/resources/META-INF/additional-spring-configuration-metadata.json)

### Bug Fixes
1. 修复代码覆盖率显示异常的 bug

## 1.1.5
1. 引入 [parent](https://github.com/drtrang/parent) 作为 Parent Pom

## 1.1.4
1. 新增 DruidDataSourceCustomizer，支持定制化数据源配置
2. 优化 DruidDataSource 初始化的日志打印时机
3. 优化 DruidDataSource 的别名，当 BeanName 的后缀不为 'DataSource' 时增加别名 

## 1.1.3
1. 修复多数据源环境下的注册失败问题

## 1.1.2
1. 改进多数据源的声明方式，由 Starter 自动发现配置并注册到 ApplicationContext

## 1.1.1
1. 新增 `statement-sql-format-option` 的代码提示

## 1.1.0
1. 新增 `druid-spring-boot-actuator-starter` 模块
3. 更改包名称为 `com.github.trang.druid.autoconfigure`
4. 更改数据源名称为 `DruidDataSource2`
2. 优化 Auto-configure Bean 的加载条件
5. 优化 Maven 依赖

## 1.0.3
1. 新增 ConfigFilter 的自动配置，替换 Druid 默认的 `connectionProperties` 方式
2. 修复 `spring.datasource.schema` 会执行两次的 bug

## 1.0.2
1. 新增 WallConfig 的自动配置
2. 新增多数据源支持

## 1.0.1 
1. 新增一部分 application.yml 的自动提示，如 `enabled`、`db-type` 等
2. 统一配置文件位置

## 1.0.0
1. 提供基于 Druid 的 Spring Boot Starter