# 「XX 的专属小天地」项目技术文档

## 1. 项目概述

本项目是一个基于 Java Spring Boot 的 Web 应用，旨在复刻并提供一个名为 “XX 的专属小天地” 的静态前端页面。项目采用服务端渲染（SSR）模式，完整保留了原设计的视觉风格与交互细节。

## 2. 技术选型

表格







| 层级         | 技术        | 版本    | 说明                                       |
| ------------ | ----------- | ------- | ------------------------------------------ |
| **核心框架** | Spring Boot | 3.2.x   | 简化配置，快速搭建 Web 服务                |
| **模板引擎** | Thymeleaf   | 3.1.x   | 用于渲染 HTML 页面，完美保留现有 HTML 结构 |
| **构建工具** | Maven       | 3.9+    | 项目依赖管理与构建                         |
| **JDK**      | OpenJDK     | 17 / 21 | 推荐使用 LTS 版本以获得长期支持            |

## 3. 项目结构

text











```
luminous-sanctuary/
├── pom.xml                                     // Maven 配置文件
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── sanctuary/
        │               ├── SanctuaryApplication.java  // 启动类
        │               └── controller/
        │                   └── PageController.java     // 页面控制器
        └── resources/
            ├── application.properties                // 配置文件
            ├── static/                               // 静态资源 (CSS/JS/Fonts 如需要本地存放)
            └── templates/
                └── index.html                        // 您的前端页面代码
```

## 4. 快速开始