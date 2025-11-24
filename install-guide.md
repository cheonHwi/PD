# SourceParser

Java 프로젝트 분석 도구

## 설치

### 일반 사용자 (추천)

#### Mac / Linux
```bash
# 원라이너 설치
curl -fsSL https://raw.githubusercontent.com/yourname/sourceparser/main/install.sh | bash
```

#### Windows
```powershell
# PowerShell에서 실행
irm https://raw.githubusercontent.com/yourname/sourceparser/main/install.ps1 | iex
```

## 사용법
```bash
# 프로젝트 분석
sourceparser /path/to/project
sourceparser .

# 도움말
sourceparser --help
```

## 제거

### Mac / Linux
```bash
rm ~/.local/bin/sourceparser
rm -rf ~/.sourceparser
```

### Windows
```powershell
Remove-Item $env:USERPROFILE\bin\sourceparser.bat
Remove-Item -Recurse $env:USERPROFILE\.sourceparser
```