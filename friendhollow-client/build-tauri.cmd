@echo off
setlocal

set "VCVARS=C:\PROGRA~2\MICROS~3\2022\BuildTools\VC\Auxiliary\Build\vcvars64.bat"
if not exist "%VCVARS%" (
  echo vcvars64 not found: %VCVARS%
  exit /b 1
)
call "%VCVARS%"
set "PATH=%USERPROFILE%\.cargo\bin;%PATH%"
npm run tauri build -- %*
exit /b %ERRORLEVEL%
