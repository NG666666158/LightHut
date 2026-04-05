# Windows 桌面打包说明（jpackage）

## 1) 一键打包

在项目根目录执行：

`mvn -Pdesktop-win clean package -DskipTests`

## 2) 产物位置

- 默认输出到：`%TEMP%\friend-hollow-jpackage\installer`
- 若系统未安装 WiX，会自动降级为 `app-image`（免安装目录版），目录为：
  - `%TEMP%\friend-hollow-jpackage\installer\FriendHollow`

## 3) 启动与停止

- 目录版启动（推荐快速体验）：
  - 双击：`FriendHollow\FriendHollow.exe`
- 脚本版启动（含端口检查、自动打开浏览器、日志）：
  - 启动（可见诊断版，不会闪退）：`packaging/windows/launch.bat`
  - 启动（静默版，适合快捷方式）：`packaging/windows/launch-silent.bat`
  - 停止：`packaging/windows/stop.bat`
  - 一键诊断：`packaging/windows/doctor.bat`

## 4) 数据目录

- 启动参数已注入上传目录到可写位置：
  - `%LOCALAPPDATA%\FriendHollow\uploads\starlight`
- 脚本日志目录：
  - `%LOCALAPPDATA%\FriendHollow\logs\desktop.log`

## 5) 想要真正 `exe` 安装包（安装向导）

当前机器若缺少 WiX，会无法输出安装型 `exe`。

- 安装 WiX Toolset 3.x 后重试：
  - `winget install WiXToolset.WiXToolset`
- 确认 `candle.exe`、`light.exe` 在 `PATH` 中后，再执行打包命令。

## 6) 常见问题

- 8080 端口被占用：先关闭占用进程再启动。
- 页面打不开：先检查应用是否启动，再访问 `http://127.0.0.1:8080/`。
- 图片上传失败：确认 `%LOCALAPPDATA%\FriendHollow\uploads\starlight` 有写权限。

## 7) 闪退/黑窗排查（推荐顺序）

1. 先双击 `packaging/windows/launch.bat`（可见窗口，会打印失败原因并停留）。
2. 若失败，双击 `packaging/windows/doctor.bat` 收集诊断信息。
3. 查看日志：`%LOCALAPPDATA%\FriendHollow\logs\desktop.log`。
4. 常见修复：
   - 缺少 Java：安装 JDK 17+ 或设置 `JAVA_HOME`；
   - 端口冲突：执行 `packaging/windows/stop.bat` 后再启动；
   - jar 不存在：先执行 `mvn package` 生成 `target/hollow-1.0.0.jar`。
