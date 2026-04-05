@echo off
setlocal
chcp 65001 >nul

for /f "tokens=5" %%a in ('netstat -ano ^| findstr /r /c:":8080 .*LISTENING"') do (
  if not "%%a"=="" (
    echo [INFO] 正在停止 8080 端口进程 PID=%%a
    taskkill /PID %%a /F >nul 2>&1
  )
)

echo [INFO] 停止完成。
exit /b 0
