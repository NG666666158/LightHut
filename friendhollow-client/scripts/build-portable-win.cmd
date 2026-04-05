@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul

set "ROOT=%~dp0..\.."
for %%I in ("%ROOT%") do set "ROOT=%%~fI"
set "CLIENT_DIR=%ROOT%\friendhollow-client"
set "DIST_DIR=%ROOT%\dist-client"
set "JAR_PATH=%ROOT%\target\hollow-1.0.0.jar"
set "EXE_PATH=%CLIENT_DIR%\src-tauri\target\release\friendhollow-client.exe"

echo [1/4] Build backend jar...
pushd "%ROOT%"
call mvn package -DskipTests -q
if errorlevel 1 (
  echo [ERROR] Maven build failed.
  exit /b 1
)
popd

echo [2/4] Build desktop client exe...
pushd "%CLIENT_DIR%"
if exist "build-tauri.cmd" (
  call build-tauri.cmd --no-bundle
) else (
  call npm run tauri build -- --no-bundle
)
if errorlevel 1 (
  echo [ERROR] Tauri build failed.
  exit /b 1
)
popd

if not exist "%JAR_PATH%" (
  echo [ERROR] Missing jar: %JAR_PATH%
  exit /b 1
)
if not exist "%EXE_PATH%" (
  echo [ERROR] Missing exe: %EXE_PATH%
  exit /b 1
)

echo [3/4] Prepare portable directory...
if exist "%DIST_DIR%" rmdir /s /q "%DIST_DIR%"
mkdir "%DIST_DIR%"
copy /y "%EXE_PATH%" "%DIST_DIR%\FriendHollow.exe" >nul
copy /y "%JAR_PATH%" "%DIST_DIR%\hollow-1.0.0.jar" >nul

(
echo @echo off
echo setlocal
echo cd /d %%~dp0
echo start "" "FriendHollow.exe"
) > "%DIST_DIR%\start-client.cmd"

(
echo # FriendHollow Portable
echo.
echo 1. Double click `start-client.cmd`
echo 2. Or run `FriendHollow.exe` directly
echo 3. Logs: %%LOCALAPPDATA%%\FriendHollow\logs\desktop.log
) > "%DIST_DIR%\README-portable.md"

echo [4/4] Done.
echo Output: %DIST_DIR%
exit /b 0
