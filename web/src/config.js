/**
 * 尚未迁移到 Vue 的 Thymeleaf 页面。
 * 开发：在 web/.env.development 设置 VITE_LEGACY_ORIGIN=http://127.0.0.1:8080
 * 生产：同域部署时可留空，使用相对路径 /cheer、/reminder。
 */
export function legacyPage(path) {
  const base = import.meta.env.VITE_LEGACY_ORIGIN
  if (base && String(base).trim()) {
    return `${String(base).replace(/\/$/, '')}${path.startsWith('/') ? path : `/${path}`}`
  }
  return path.startsWith('/') ? path : `/${path}`
}
