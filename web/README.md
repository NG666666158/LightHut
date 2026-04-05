# friend-hollow-web（Vue 3 + Vite + Pinia）

## 前置条件

1. 先启动后端 Spring Boot（默认 `http://127.0.0.1:8080`）。
2. Node.js 18+。

## 开发

```bash
cd web
npm install
npm run dev
```

浏览器打开 Vite 提示的地址（一般为 `http://localhost:5173`）。

### 路由与权限

| 路径 | 说明 |
|------|------|
| `/` | 已登录 → 跳转 `/home`；未登录 → `/starlight` |
| `/home` | **需登录**：对接 `GET /api/home/overview`、签到、心情记录 |
| `/starlight` | 匿名可浏览星光墙列表（`GET /api/starlight/memories`） |
| `/login` | 表单登录（POST `/login`） |

### 状态（Pinia）

- `useAuthStore`：会话、`/api/auth/me`、退出
- `useHomeStore`：首页概览、签到、心情 POST

### 与 Thymeleaf 经典版并存（全站切换过渡期）

打气站、温柔提醒仍在后端 Thymeleaf。开发时在 `web/.env.development` 配置：

```env
VITE_LEGACY_ORIGIN=http://127.0.0.1:8080
```

顶栏「打气站」「温柔提醒」会打开该源下的 `/cheer`、`/reminder`。生产同域部署时可删去该变量，改为相对路径 `/cheer`、`/reminder`（由 Nginx 交给 Spring）。

代理说明见 `vite.config.js`：`/api`、`/login`、`/logout`、`/uploads` 会转发到后端。

## 构建

```bash
npm run build
```

产物在 `web/dist/`。生产环境由 Nginx 托管 SPA，并对上述路径反代到 Spring Boot。
