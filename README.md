
# PD - SourceParser
Java 프로젝트 분석 도구

## 📋 프로젝트 개요

### 🎯 목표
Java 프로젝트의 **클래스 구조**, **메서드 호출 관계**, **파일 간 의존성**을 분석하여  
**JSON 형식으로 출력하는 CLI 기반 소스코드 분석 도구**

### ✨ 핵심 기능
1. Java 소스코드 파싱 (class, interface, enum, record)
2. 메서드 시그니처 및 파라미터 분석
3. 메서드 간 호출 관계 추적
4. 클래스 간 의존성 자동 분석
5. 어노테이션(@Override, @Test, Spring 어노테이션) 감지
6. 파싱 에러 추적 및 로깅
7. JSON 형식 결과 출력
8. NPM 스타일 프로그레스 바 제공

### ⚠️ 제약사항
- **바닐라 Java 17만 사용** (외부 라이브러리 없음)
- **프로토타입 수준** (Java 전체 문법 지원 X)

---

## 🚀 빠른 시작

### 📦 설치
```bash
# Unix/Linux/Mac
curl -fsSL https://raw.githubusercontent.com/cheonHwi/PD/main/install.sh | bash

# Windows
irm https://raw.githubusercontent.com/cheonHwi/PD/main/install.ps1 | iex
```

### ▶️ 사용법
```bash
# 특정 프로젝트 분석
sourceparser /path/to/java/project

# 현재 디렉토리 분석
sourceparser .
```

### 📄 출력 예시
```
📂 Analyzing: /Users/user/Documents/MyProject
📦 Project: MyProject

🔍 Parsing files... [====================] 100% (30/30) - 3s
🔗 Resolving dependencies... ✓
📝 Generating JSON... ✓

✨ Analysis complete!

📊 Statistics:
   Project:         MyProject
   Total files:     30
   ✓ Success:       28
   ✗ Failed:        2
   Classes parsed:  28
   Methods:         167
   Dependencies:    52
   Time:            4s

📄 Output: /Users/user/MyProject/MyProject-2024-11-24-153045.json
```

---

## 🏗 전체 아키텍처
```
소스 파일들
    ↓
FileAnalyzer (디렉토리 순회)
    ↓
Tokenizer (문자열 → Token)
    ↓
Lexer (토큰 탐색)
    ↓
ClassParser (클래스 파싱)
    ├─ MethodParser (시그니처 파싱)
    └─ MethodCallTracker (호출 추적)
    ↓
DependencyResolver (의존성 분석)
    ↓
JsonSerializer (JSON 생성)
    ↓
project-timestamp.json
```

---

## 📦 컴포넌트 구성

### 1. Tokenizer ✅
- 키워드, 식별자, 리터럴, 연산자, 제네릭 `< > ?`, 어노테이션 `@` 처리

### 2. Lexer ✅
- 토큰 이동 및 문법 검증, lookahead 지원

### 3. Parser(ClassParser) ✅
- package
- import
- class/interface/enum/record
- extends / implements
- method 파싱

### 4. MethodParser ✅
- 접근 제한자, 수식어
- 리턴 타입
- 제네릭, 중첩 제네릭, 와일드카드
- 배열, varargs
- 파라미터 목록

### 5. MethodCallTracker ✅
- object.method()
- ClassName.staticMethod()
- this.method()
- super.method()

### 6. FileAnalyzer ✅
- 디렉토리 재귀 탐색 및 .java 수집

### 7. ProjectAnalyzer ✅
- 전체 파싱 orchestrator
- 파싱 성공/실패 추적
- 로그 및 프로그레스 바

### 8. DependencyResolver ✅
- extends / implements / import 기반 의존성 분석
- 메서드 호출 기반 의존성 연결

### 9. JsonSerializer ✅
- 분석 결과 JSON 생성

---

## 📦 데이터 모델

### ClassInfo
```json
{
  "packageName": "",
  "className": "",
  "classType": "",
  "accessModifier": "",
  "modifiers": [],
  "extendsClass": "",
  "implementsList": [],
  "imports": [],
  "methods": [],
  "dependencies": []
}
```

### MethodInfo
```json
{
  "methodName": "",
  "accessModifier": "",
  "modifiers": [],
  "returnType": "",
  "lineNumber": 0,
  "parameters": [],
  "methodCalls": []
}
```

### MethodCall
```json
{
  "targetClass": "",
  "targetMethod": "",
  "line": 0
}
```

---

## 🎯 지원 범위

### 완전 지원
- 클래스/인터페이스/enum/record
- 패키지/import
- 제네릭/중첩 제네릭/와일드카드
- 배열/가변인자
- 어노테이션 존재 여부

### 부분 지원
- 생성자 스킵
- 멤버 변수 스킵
- 단순 패턴의 메서드 호출만 분석

### 미지원
- 람다
- Stream API
- 내부 클래스 / 익명 클래스
- 어노테이션 값 파싱


## 🧱 프로젝트 구조
```
PD/
├── src/main/java/
│   ├── SourceParser/
│   ├── Analyzer/
│   └── ProgressBar/
├── src/test/java/
├── build.gradle
├── install.sh
├── install.ps1
└── README.md
```

---

## 🚀 구현 로드맵

### Phase 1: 기반 구조 ✅
### Phase 2: 클래스 파싱 ✅
### Phase 3: 파일 분석 ✅
### Phase 4: 메서드 호출 추적 ✅
### Phase 5: JSON 출력 ✅
### Phase 6: 웹 UI 시각화(예정)

---

## 🔧 개발 환경
- Java 17+
- Gradle 8.0+


