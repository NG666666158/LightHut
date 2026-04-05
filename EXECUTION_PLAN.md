# 个人站重构 — 执行计划表

按顺序执行；完成后将对应行勾选。

| 阶段 | 内容 | 状态 |
|------|------|------|
| 1 | 依赖：`spring-boot-starter-security`、`spring-boot-starter-data-jpa`、H2、`thymeleaf-extras-springsecurity6` | 已完成 |
| 2 | 配置：`application.properties` 数据源、JPA、`app.security.admin.*` | 已完成 |
| 3 | 持久化：`Reminder` / `Mood` / `Memory` / `SignInState` 实体与 JPA 实现，移除 InMemory 的 Spring 注册 | 已完成 |
| 4 | 安全：`SecurityFilterChain`、单管理员、`/login`、API 401、CSRF Cookie、公开 `GET /api/starlight/memories` | 已完成 |
| 5 | 前端：落地页跳转登录、`fetch` 带 CSRF、星光墙上传/删除仅登录可见 | 已完成 |
| 6 | （后续）Vue 3 + Vite 工程与逐页迁移 | 未开始 |
| 7 | （后续）生产 Nginx + HTTPS + 可选 PostgreSQL  profile | 未开始 |

**默认管理员**：用户名 `admin`，密码 `changeme`（`{noop}`，仅开发用；上线请改为 BCrypt 并保密）。

**数据文件**：H2 文件库默认 `./data/hollow`（相对应用工作目录）。
