# 简单说明
我自己的代码生成方面做的一些尝试。

这个程序可以快速生成基于 Spring Boot 的 REST API，生成的 REST API 只具备简单的 CRUD 功能。

## 模块说明
* `core` ：该模块实现的是主要的代码生成逻辑
* `ui-swing`：基于 Swing 的用户界面
* `ui-spring-boot`：基于 Spring Boot 的 Web 界面（未完成）
* `ui-web`：Web 界面（未完成）
* `bridge`：类似消息总线的东西

## 运行方法
目前已经完整实现的用户界面只有 `ui-swing`，运行 `ui-swing` 中的 `cn.dyr.rest.generator.ui.swing.SwingUIApplication` 可以以桌面程序的方式启动

## 一些说明
* 在代码生成以后的打包功能需要 Maven，并且 Maven 相关的环境变量也必须配置好。（`PATH` 环境变量中有 `mvn` 的路径）