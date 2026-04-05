$ErrorActionPreference = "Stop"

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Resolve-Path (Join-Path $scriptDir "..\..")
$targetJar = Join-Path $projectRoot "target\hollow-1.0.0.jar"

if (-not (Test-Path $targetJar)) {
    throw "Jar not found: $targetJar"
}

$stageRoot = Join-Path $env:TEMP "friend-hollow-jpackage"
$inputDir = Join-Path $stageRoot "input"
$destDir = Join-Path $stageRoot "installer"

if (Test-Path $stageRoot) {
    Remove-Item $stageRoot -Recurse -Force
}
New-Item -ItemType Directory -Path $inputDir -Force | Out-Null
New-Item -ItemType Directory -Path $destDir -Force | Out-Null

Copy-Item $targetJar (Join-Path $inputDir "hollow-1.0.0.jar") -Force

$hasLight = $null -ne (Get-Command "light.exe" -ErrorAction SilentlyContinue)
$hasCandle = $null -ne (Get-Command "candle.exe" -ErrorAction SilentlyContinue)
$packageType = if ($hasLight -and $hasCandle) { "exe" } else { "app-image" }

if ($packageType -ne "exe") {
    Write-Host "WiX not found, fallback to app-image."
}

$arguments = @(
    "--type", $packageType,
    "--name", "FriendHollow",
    "--vendor", "FriendHollow",
    "--app-version", "1.0.0",
    "--input", $inputDir,
    "--dest", $destDir,
    "--main-jar", "hollow-1.0.0.jar",
    "--main-class", "com.friend.hollow.HollowApplication",
    "--java-options", "-Dfile.encoding=UTF-8",
    "--java-options", "-Dapp.starlight.upload-dir=${env:LOCALAPPDATA}\FriendHollow\uploads\starlight"
)

if ($packageType -eq "exe") {
    $arguments += @("--win-shortcut", "--win-menu", "--win-dir-chooser")
}

Write-Host "Running jpackage..."
& jpackage @arguments

Write-Host "Package generated at: $destDir"
