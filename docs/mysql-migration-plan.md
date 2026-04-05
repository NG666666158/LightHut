# 数据库从 H2 迁移到 MySQL — 执行计划

本文档说明如何将 [挚友专属・治愈树洞备忘录](../) 的持久层从嵌入式 **H2 文件库** 切换为 **MySQL**，并给出数据迁移与上线检查项。  
当前 JPA 实体见 `src/main/java/com/friend/hollow/entity/`（签到状态、星光墙回忆、心情、温柔提醒、每日拥抱计数等）。

---

## 1. 目标与范围

| 项目 | 说明 |
|------|------|
| 目标 | 生产或长期对外访问时使用 MySQL，便于备份、权限与扩展 |
| 不涉及 | 业务代码大规模重写（JPA 实体与 Repository 可继续沿用） |
| 需单独备份 | `uploads/starlight` 等上传文件与 H2 库无关，迁移库后路径配置不变 |

---

## 2. 前置条件

1. 安装 **MySQL 8.0+**（或兼容的云 RDS）。
2. 创建数据库与用户（示例）：

```sql
CREATE DATABASE hollow CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'hollow'@'%' IDENTIFIED BY '你的强密码';
GRANT ALL PRIVILEGES ON hollow.* TO 'hollow'@'%';
FLUSH PRIVILEGES;
```

3. 确认服务器与应用之间网络、防火墙、白名单（云厂商）已放行 **3306**（或自定义端口）。

---

## 3. 工程改动（依赖 + 配置）

### 3.1 Maven

已在根 `pom.xml` 增加 **`mysql-connector-j`**（`runtime`）。H2 仍保留，本地默认配置可继续用 H2 开发。

### 3.2 Spring Profile：`mysql`

1. 复制 [`src/main/resources/application-mysql.example.properties`](../src/main/resources/application-mysql.example.properties) 为 **`application-mysql.properties`**（勿提交真实密码；可加入个人 `.gitignore`）。
2. 填写 JDBC URL、用户名、密码。
3. 启动时激活 Profile，任选其一：

```bash
# 命令行
java -jar hollow-1.0.0.jar --spring.profiles.active=mysql

# Maven
mvn spring-boot:run -Dspring-boot.run.profiles=mysql

# 环境变量
set SPRING_PROFILES_ACTIVE=mysql
```

主配置 [`application.properties`](../src/main/resources/application.properties) 仍为 **H2**，未改默认行为。

### 3.3 `ddl-auto` 建议

| 阶段 | 建议 | 说明 |
|------|------|------|
| 首次连 MySQL、表为空 | `update` | Hibernate 根据实体建表，与当前 H2 行为一致 |
| 已有表、要严控结构 | `validate` + Flyway/Liquibase | 避免实体与库不一致时被静默改表 |

示例文件中已写 `spring.jpa.hibernate.ddl-auto=update`，上线稳定后可改为 `validate` 并引入版本化脚本（可选后续任务）。

### 3.4 时区与字符集

- 连接 URL 中建议使用 `serverTimezone=Asia/Shanghai`（或与业务一致的时区）。
- 库级已建议 `utf8mb4`，避免中文与 emoji 存储问题。
- 应用层 [`application.properties`](../src/main/resources/application.properties) 中 `hibernate.jdbc.time_zone=UTC` 在 MySQL Profile 中可改为与 JVM/业务一致，或保持 UTC 并在代码中统一用 `ZoneId`（你项目中「今日拥抱」已使用 `Asia/Shanghai`）。

---

## 4. 数据迁移策略（三选一）

### 方案 A：全新库（最简单）

适用于可丢弃 H2 中旧数据，或数据量很小可手工补录。

1. 用 `mysql` Profile 启动一次，让 Hibernate `update` 建表。
2. 不再迁移 H2 数据。

### 方案 B：H2 导出 SQL 再导入 MySQL（中等）

1. 用 H2 Console 或命令行对原库执行 `SCRIPT` 导出，或按表导出 CSV。
2. **手工调整** SQL：类型、引号、自增、保留字等与 MySQL 差异较大，需逐段检查。
3. 表结构若已由 JPA 建好，可只导入 **数据行**（`INSERT`），注意外键与顺序。

### 方案 C：一次性迁移脚本 / ETL（数据量大）

编写小工具：读 H2 → 写 MySQL（或导出 CSV + `LOAD DATA`）。适合表多、数据多的情况。

**建议**：个人站首次上 MySQL 多用 **方案 A**；若 H2 里已有大量星光墙与提醒，再考虑 B/C。

---

## 5. 验证清单（迁移后必测）

- [ ] 启动无报错，日志中数据源为 MySQL。
- [ ] `/home` 概览、签到、心情提交。
- [ ] `/starlight` 列表与上传（文件仍在 `uploads/starlight`）。
- [ ] `/reminder` 月历与待办。
- [ ] `/cheer`「想要抱抱」今日计数 GET/POST。
- [ ] 备份：对 MySQL 做 `mysqldump` 试恢复一次。

---

## 6. 回滚

保留原 `data/hollow.mv.db` 与一份可启动的 jar；若 MySQL 异常，临时改回无 `mysql` Profile 并恢复 H2 配置即可（数据以当时 H2 快照为准）。

---

## 7. 可选后续（非必须）

- 引入 **Flyway**：`ddl-auto=validate`，表结构版本化。
- 生产关闭 `spring.thymeleaf.cache=false`，改为 `true`。
- 敏感配置改用环境变量或 Spring Cloud Config，避免 `application-mysql.properties` 入库。

---

## 8. 相关文件索引

| 文件 | 作用 |
|------|------|
| `pom.xml` | MySQL 驱动依赖 |
| `src/main/resources/application.properties` | 默认 H2 |
| `src/main/resources/application-mysql.example.properties` | MySQL 模板（复制为 `application-mysql.properties`） |
| `src/main/java/com/friend/hollow/entity/*.java` | 表结构来源 |

按顺序完成 **§2 → §3 → §4 → §5** 即可完成迁移闭环。

云上容器化部署见 **[docker-deploy.md](docker-deploy.md)**（`docker-compose.yml` + MySQL 卷与环境变量）。
