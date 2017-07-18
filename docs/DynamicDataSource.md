# Dynamic Data Source

1.0.2 版本中新增多数据源支持，开启该特性需要手动声明各个 DataSource，并指定注入的前缀（`spring.datasource.druid.${name}`），
其中 `${name}` 可根据实际情况自定义。


## 使用方式
1. 手动声明 DataSource，类型为 `com.github.trang.druid.datasource.DruidMultiDataSource`，该 Bean 会继承 `spring.datasource.druid` 的配置，
并且注入 `spring.datasource.druid.one` 的配置，若名称相同则覆盖 `spring.datasource.druid`。

```java
@Bean
@ConfigurationProperties("spring.datasource.druid.one")
public DruidDataSource firstDataSource() {
    return new DruidMultiDataSource();
}

@Bean
@ConfigurationProperties("spring.datasource.druid.two")
public DruidDataSource secondDataSource() {
    return new DruidMultiDataSource();
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


## 示例
将 [druid-spring-boot-samples](https://github.com/drtrang/druid-spring-boot/tree/master/druid-spring-boot-samples) 中 
[application.yml](https://github.com/drtrang/druid-spring-boot/blob/master/druid-spring-boot-samples/src/main/resources/application.yml) 
的 `spring.profiles.active` 配置改为 `dynamic` 即可。