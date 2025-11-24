# install.ps1
$ErrorActionPreference = "Continue"  # ← Stop에서 Continue로 변경

$VERSION = "1.0.0"
$GITHUB_REPO = "cheonHwi/PD"
$JAR_URL = "https://github.com/$GITHUB_REPO/releases/download/v$VERSION/sourceparser.jar"

$INSTALL_DIR = "$env:USERPROFILE\bin"
$JAR_DIR = "$env:USERPROFILE\.sourceparser"

function Write-Step {
    param($Step, $Total, $Message)
    Write-Host "[$Step/$Total] $Message" -ForegroundColor Cyan
}

function Write-Success {
    param($Message)
    Write-Host "✓ $Message" -ForegroundColor Green
}

function Write-Error-Custom {
    param($Message)
    Write-Host "❌ $Message" -ForegroundColor Red
}

function Write-Warning-Custom {
    param($Message)
    Write-Host "⚠️  $Message" -ForegroundColor Yellow
}

Write-Host "📦 Installing SourceParser v$VERSION..." -ForegroundColor Green
Write-Host ""

# 1. Java 확인 (더욱 단순화)
Write-Step 1 6 "Checking Java..."

# 사용자에게 Java 확인 요청
Write-Host "Please run 'java -version' to verify Java is installed:" -ForegroundColor Yellow
Write-Host "  PS> java -version" -ForegroundColor Cyan
Write-Host ""

$confirmation = Read-Host "Is Java 17+ installed and working? (Y/n)"
if ($confirmation -eq 'n' -or $confirmation -eq 'N') {
    Write-Error-Custom "Java 17+ is required"
    Write-Host "Download: https://adoptium.net/" -ForegroundColor Cyan
    exit 1
}

Write-Success "Java check skipped (user confirmed)"

# 2. 디렉토리 생성
Write-Step 2 6 "Creating directories..."
New-Item -ItemType Directory -Force -Path $INSTALL_DIR | Out-Null
New-Item -ItemType Directory -Force -Path $JAR_DIR | Out-Null
Write-Success "Directories created"

# 3. JAR 다운로드
Write-Step 3 6 "Downloading sourceparser.jar..."
Write-Host "URL: $JAR_URL" -ForegroundColor Yellow

try {
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri $JAR_URL -OutFile "$JAR_DIR\sourceparser.jar"

    $jarSize = (Get-Item "$JAR_DIR\sourceparser.jar").Length

    if ($jarSize -lt 1000) {
        Write-Error-Custom "Downloaded file is too small ($jarSize bytes)"
        Remove-Item "$JAR_DIR\sourceparser.jar"
        exit 1
    }

    Write-Success "Downloaded ($jarSize bytes)"
} catch {
    Write-Error-Custom "Download failed: $_"
    exit 1
}

# 4. 배치 파일 생성
Write-Step 4 6 "Creating executable script..."
$batchContent = @'
@echo off
java -jar "%USERPROFILE%\.sourceparser\sourceparser.jar" %*
'@
Set-Content -Path "$INSTALL_DIR\sourceparser.bat" -Value $batchContent
Write-Success "Script created"

# 5. 권한 설정
Write-Step 5 6 "Setting permissions..."
Write-Success "Batch file is executable"

# 6. 설치 검증
Write-Step 6 6 "Verifying installation..."

if ((Test-Path "$JAR_DIR\sourceparser.jar") -and (Test-Path "$INSTALL_DIR\sourceparser.bat")) {
    Write-Success "All files installed correctly"
} else {
    Write-Error-Custom "Installation verification failed"
    exit 1
}

Write-Host ""
Write-Host "✨ SourceParser installed successfully!" -ForegroundColor Green
Write-Host ""

# PATH 확인 및 추가
$userPath = [Environment]::GetEnvironmentVariable("Path", "User")
if ($userPath -notlike "*$INSTALL_DIR*") {
    Write-Host "⚠️  Adding to PATH..." -ForegroundColor Yellow
    [Environment]::SetEnvironmentVariable(
        "Path",
        "$userPath;$INSTALL_DIR",
        "User"
    )
    Write-Success "Added to PATH"
    Write-Host ""
    Write-Warning-Custom "Restart your terminal for PATH changes to take effect"
} else {
    Write-Success "Already in PATH"
}

Write-Host ""
Write-Host "Usage:" -ForegroundColor Cyan
Write-Host "  sourceparser C:\path\to\project"
Write-Host "  sourceparser ."
Write-Host ""
Write-Host "Test:" -ForegroundColor Cyan
Write-Host "  Close and reopen PowerShell, then run:"
Write-Host "  sourceparser --version"