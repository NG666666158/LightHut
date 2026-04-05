@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul

set "SILENT_MODE=%~1"
set "APP_NAME=FriendHollow"
set "APP_PORT=8080"
set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..\..") do set "BASE_DIR=%%~fI"
set "JAVA_CMD="
set "APP_HOME=%LOCALAPPDATA%\FriendHollow"
set "APP_UPLOAD_DIR=%APP_HOME%\uploads\starlight"
set "APP_LOG_DIR=%APP_HOME%\logs"
set "APP_RUNTIME_DIR=%APP_HOME%\runtime"
set "APP_JAR=%APP_RUNTIME_DIR%\hollow-1.0.0.jar"
set "SRC_JAR=%BASE_DIR%\target\hollow-1.0.0.jar"

call :banner
call :resolve_java || goto fail
call :check_source_jar || goto fail
call :check_port_free || goto fail
call :prepare_dirs || goto fail
call :copy_runtime_jar || goto fail
call :start_app || goto fail
call :wait_port_ready || goto fail
call :open_browser
goto success

:banner
echo.
echo ==== %APP_NAME% Launcher ====
echo [INFO] BASE_DIR      = %BASE_DIR%
echo [INFO] SRC_JAR       = %SRC_JAR%
echo [INFO] APP_JAR       = %APP_JAR%
echo [INFO] APP_LOG_DIR   = %APP_LOG_DIR%
echo [INFO] APP_UPLOADDIR = %APP_UPLOAD_DIR%
echo.
exit /b 0

:resolve_java
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
if not defined JAVA_CMD (
  for /f "usebackq delims=" %%I in (`powershell -NoProfile -Command "$c=Get-Command java -ErrorAction SilentlyContinue; if($c){$c.Source}"`) do set "JAVA_CMD=%%I"
)
if not defined JAVA_CMD (
  echo [ERROR] 未找到 Java。请安装 JDK 17+ 或配置 JAVA_HOME。
  exit /b 1
)
if not exist "%JAVA_CMD%" (
  echo [ERROR] Java 路径无效：%JAVA_CMD%
  exit /b 1
)
echo [INFO] JAVA_CMD      = %JAVA_CMD%
exit /b 0

:check_source_jar
if not exist "%SRC_JAR%" (
  echo [ERROR] 未找到 target\hollow-1.0.0.jar，请先执行: mvn package
  exit /b 1
)
exit /b 0

:check_port_free
for /f "tokens=5" %%a in ('netstat -ano ^| findstr /r /c:":%APP_PORT% .*LISTENING"') do (
  if not "%%a"=="" (
    echo [ERROR] 端口 %APP_PORT% 已被占用（PID=%%a）。
    exit /b 1
  )
)
exit /b 0

:prepare_dirs
if not exist "%APP_UPLOAD_DIR%" mkdir "%APP_UPLOAD_DIR%"
if not exist "%APP_LOG_DIR%" mkdir "%APP_LOG_DIR%"
if not exist "%APP_RUNTIME_DIR%" mkdir "%APP_RUNTIME_DIR%"
exit /b 0

:copy_runtime_jar
copy /Y "%SRC_JAR%" "%APP_JAR%" >nul
if not exist "%APP_JAR%" (
  echo [ERROR] 复制运行包失败：%APP_JAR%
  exit /b 1
)
exit /b 0

:start_app
echo [INFO] 正在后台启动 %APP_NAME% ...
start "" /b cmd /c ""%JAVA_CMD%" -Dfile.encoding=UTF-8 -Dapp.starlight.upload-dir="%APP_UPLOAD_DIR%" -jar "%APP_JAR%" >> "%APP_LOG_DIR%\desktop.log" 2>&1"
exit /b 0

:wait_port_ready
set "MAX_RETRY=40"
set "COUNT=0"
:wait_loop
set /a COUNT+=1
for /f "tokens=5" %%a in ('netstat -ano ^| findstr /r /c:":%APP_PORT% .*LISTENING"') do if not "%%a"=="" exit /b 0
if %COUNT% GEQ %MAX_RETRY% (
  echo [ERROR] 启动超时：未检测到端口 %APP_PORT% 进入监听状态。
  echo [INFO] 最近日志：
  powershell -NoProfile -Command "if (Test-Path '%APP_LOG_DIR%\desktop.log') { Get-Content '%APP_LOG_DIR%\desktop.log' -Tail 30 }"
  exit /b 1
)
ping -n 2 127.0.0.1 >nul
goto wait_loop

:open_browser
echo [INFO] 服务启动成功，正在打开浏览器...
start "" "http://127.0.0.1:%APP_PORT%/"
exit /b 0

:fail
echo.
echo [FAIL] 启动失败。建议先运行 packaging\windows\doctor.bat 诊断。
if /i not "%SILENT_MODE%"=="--silent" pause
exit /b 1

:success
echo [OK] 启动完成。
if /i not "%SILENT_MODE%"=="--silent" pause
exit /b 0
