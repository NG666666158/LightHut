@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul

set "APP_PORT=8080"
set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..\..") do set "BASE_DIR=%%~fI"
set "SRC_JAR=%BASE_DIR%\target\hollow-1.0.0.jar"
set "APP_HOME=%LOCALAPPDATA%\FriendHollow"
set "APP_LOG_DIR=%APP_HOME%\logs"
set "APP_UPLOAD_DIR=%APP_HOME%\uploads\starlight"

echo.
echo ==== FriendHollow Doctor ====

echo [CHECK] Java
powershell -NoProfile -Command "$c=Get-Command java -ErrorAction SilentlyContinue; if($c){Write-Output ('[ OK ] java=' + $c.Source)} else {Write-Output '[FAIL] java not found'; exit 1}"
if not "%ERRORLEVEL%"=="0" goto done

echo [CHECK] Source jar
if exist "%SRC_JAR%" (
  echo [ OK ] %SRC_JAR%
) else (
  echo [FAIL] 未找到 %SRC_JAR%
)

echo [CHECK] Port %APP_PORT%
set "PORT_BUSY="
for /f "tokens=5" %%a in ('netstat -ano ^| findstr /r /c:":%APP_PORT% .*LISTENING"') do (
  if not "%%a"=="" (
    set "PORT_BUSY=1"
    echo [WARN] 端口占用 PID=%%a
  )
)
if not defined PORT_BUSY echo [ OK ] 端口空闲

echo [CHECK] Writable directories
if not exist "%APP_LOG_DIR%" mkdir "%APP_LOG_DIR%" >nul 2>&1
if not exist "%APP_UPLOAD_DIR%" mkdir "%APP_UPLOAD_DIR%" >nul 2>&1
if exist "%APP_LOG_DIR%" (
  echo [ OK ] %APP_LOG_DIR%
) else (
  echo [FAIL] 无法创建 %APP_LOG_DIR%
)
if exist "%APP_UPLOAD_DIR%" (
  echo [ OK ] %APP_UPLOAD_DIR%
) else (
  echo [FAIL] 无法创建 %APP_UPLOAD_DIR%
)

echo [CHECK] Recent launcher logs
if exist "%APP_LOG_DIR%\desktop.log" (
  powershell -NoProfile -Command "Get-Content '%APP_LOG_DIR%\desktop.log' -Tail 20"
) else (
  echo [INFO] 暂无日志文件
)

:done
echo.
echo ==== Doctor Done ====
pause
exit /b 0
