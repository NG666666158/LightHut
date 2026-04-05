# 与「mvn spring-boot:run」配合：监视 src 下文件变更并自动执行 mvn compile，
# DevTools 检测到 target/classes 更新后会重启内嵌 Tomcat。
# 用法：终端 A 运行 mvn spring-boot:run；终端 B 运行：
#   powershell -NoProfile -ExecutionPolicy Bypass -File scripts/dev-watch-compile.ps1

$ErrorActionPreference = "Continue"
$projectRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
Set-Location -LiteralPath $projectRoot

Write-Host "监视目录: $projectRoot\src"
Write-Host "请保持另一终端已执行: mvn spring-boot:run"
Write-Host "按 Ctrl+C 停止监视。"
Write-Host ""

$lastHash = ""
while ($true) {
    Start-Sleep -Seconds 1
    $files = Get-ChildItem -Path (Join-Path $projectRoot "src") -Recurse -File -ErrorAction SilentlyContinue |
        Where-Object { $_.FullName -notmatch '\\target\\' }
    $h = ($files | ForEach-Object { "$($_.FullName)|$($_.LastWriteTimeUtc.Ticks)" }) -join "`n"
    if ($h -eq $lastHash) {
        continue
    }
    if ($lastHash -ne "") {
        Write-Host ("[{0}] 检测到变更，正在 compile..." -f (Get-Date -Format "HH:mm:ss"))
        & mvn -q -DskipTests compile
        if ($LASTEXITCODE -ne 0) {
            Write-Host "compile 失败，退出码 $LASTEXITCODE"
        }
    }
    $lastHash = $h
}
