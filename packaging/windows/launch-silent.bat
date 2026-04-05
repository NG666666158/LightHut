@echo off
setlocal
chcp 65001 >nul
call "%~dp0launch.bat" --silent
exit /b %ERRORLEVEL%
