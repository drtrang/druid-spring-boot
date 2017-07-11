# Changelog

## 1.1.1
1. 完善注释和单元测试

## 1.1.0 2017-03-16
1. 去除 Guava 依赖
2. 增加 Bean 与 Map 互相转换的工具
3. 发布到 Maven 中央仓库

## 1.0.3 2016-10-30
1. CopierAdapter 去掉不实用的 `reverse()` 方法
2. Copier 接口新增转换 List 的方法 `map()`
3. 新增 EasyMapper 新特性，抽象到 MapperCopierSupport 类

## 1.0.2 2016-10-28
1. 修复 EasyMapper 不能逆向拷贝的bug
2. 更改 Cglib 包为可选，需要使用 BeanCopier 时可自行添加依赖
3. 更新字节码依赖为最新版本

## 1.0.1 2016-10-19
1. 增加 EasyMapper 的几个常用特性

## 1.0.0 2016-10-17
1. 将 Copier 抽象出来，实现基于 Cglib 和 EasyMapper 的拷贝工具
2. 添加 CopierAdapter，代替直接实现 Copier 接口