# PD
Program Director - 우아한 테크코스 오픈 미션

## 설치방법
- 📦 [설치 가이드](install-guide.md)

## 📋 프로젝트 개요

### 목표
Java 프로젝트의 소스코드를 분석하여 파일 간 의존성과 메서드 호출 관계를 시각화하는 도구

### 핵심 기능
1. Java 소스코드 파싱 (클래스, 메서드 정보 추출)
2. 메서드 호출 관계 추적
3. 파일 간 의존성 그래프 생성
4. JSON 형식으로 결과 출력
5. UI를 통한 계층적 시각화

### 제약사항
- 바닐라 Java (외부 라이브러리 의존성 없음)
- 프로토타입 수준 구현 (완전한 Java 문법 지원 불필요)

## 🏗 전체 아키텍처
```
소스 파일들
    ↓
Analyzer (디렉토리 순회)
    ↓
ClassParser (클래스 정보 파싱)
    ↓
MethodParser (메서드 정보 파싱)
    ↓
MethodCallTracker (호출 관계 추적)
    ↓
DependencyResolver (의존성 연결)
    ↓
JsonSerializer (JSON 출력)
    ↓
project_graph.json
```

## 📦 컴포넌트 구성

### ✅ 구현 완료

#### 1. Tokenizer
- 소스코드 문자열을 Token 스트림으로 변환
- 키워드, 식별자, 심볼, 제네릭 관련 토큰 처리

#### 2. Lexer
- Token 스트림 탐색 및 문법 검증 지원
- `getCurrentToken()`, `moveForward()`, `check()`, `match()` 등 제공

#### 3. MethodParser
- 메서드 시그니처 파싱
- 접근 제한자, 리턴 타입, 메서드명, 파라미터 추출
- 배열, 제네릭, 중첩 제네릭, 와일드카드 지원

#### 4. 데이터 모델
- `MethodInfo`: 메서드 정보
- `Parameter`: 파라미터 정보
- `ClassInfo`: 클래스 정보 (구조만 정의됨)
- `MethodCall`: 메서드 호출 정보 (구조만 정의됨)

### 🔄 구현 예정

#### 1. ClassParser (다음 단계)
**역할:**
- 클래스 전체 구조 파싱
- package 선언 추출
- import 문 목록 수집
- class 선언부 파싱 (클래스명, extends, implements)
- 클래스 내 메서드 목록 추출 (MethodParser 활용)

**주요 기능:**
- `parseClass()`: 전체 클래스 파싱
- `parsePackage()`: 패키지명 추출
- `parseImports()`: import 목록 수집
- `parseClassName()`: 클래스명 추출
- `parseMethods()`: 메서드 목록 파싱

#### 2. Analyzer
**역할:**
- 프로젝트 디렉토리 순회
- Java 파일 필터링 및 읽기
- 각 파일에 대해 ClassParser 실행
- 파싱 결과를 ClassInfo 리스트로 수집

#### 3. MethodCallTracker
**역할:**
- 메서드 본문 내에서 다른 메서드 호출 추적
- `object.method()` 패턴 인식
- MethodCall 정보 생성

#### 4. DependencyResolver
**역할:**
- 파일 간 의존성 그래프 구성
- 메서드 호출을 실제 클래스와 연결
- import 문과 패키지 정보를 활용한 클래스 매핑

#### 5. JsonSerializer
**역할:**
- ClassInfo 리스트를 JSON으로 직렬화
- 파일로 저장

## 🚀 구현 로드맵

### Phase 1: 기반 구조 ✅
- [x] Tokenizer 구현
- [x] Lexer 구현
- [x] MethodParser 구현
- [x] 데이터 모델 정의
- [x] 단위 테스트 작성

### Phase 2: 클래스 파싱 (진행 중)
- [ ] ClassParser 구현
    - [ ] package 파싱
    - [ ] import 파싱
    - [ ] class 선언부 파싱
    - [ ] 메서드 목록 추출
- [ ] ClassParser 테스트 작성

### Phase 3: 파일 분석
- [ ] Analyzer 구현
- [ ] 디렉토리 순회 및 파일 파싱
- [ ] 통합 테스트

### Phase 4: 메서드 호출 추적
- [ ] MethodCallTracker 구현
- [ ] DependencyResolver 구현
- [ ] 의존성 그래프 생성

### Phase 5: JSON 출력
- [ ] JsonSerializer 구현
- [ ] 최종 통합 테스트

### Phase 6: UI 시각화 (선택)
- [ ] JSON 기반 그래프 시각화
- [ ] 계층적 접기/펼치기 기능

## 📊 데이터 흐름
```
UserService.java
    ↓ (ClassParser)
ClassInfo {
    packageName: "com.example.service"
    className: "UserService"
    imports: [...]
    methods: [
        MethodInfo {
            name: "getUsers"
            returnType: "List<User>"
            parameters: [...]
            methodCalls: [...]  ← MethodCallTracker가 채움
        }
    ]
    dependencies: [...]  ← DependencyResolver가 채움
}
    ↓ (JsonSerializer)
project_graph.json
```

## 🎯 ClassParser 구현 방향

### 파싱 순서
1. `package` 선언 찾기 → 패키지명 추출
2. `import` 키워드 반복 찾기 → import 목록 수집
3. `class` 키워드 찾기 → 클래스명 추출
4. `extends` 확인 → 상속 클래스 추출
5. `implements` 확인 → 인터페이스 목록 추출
6. `{` 내부에서 메서드 시그니처 찾기 → MethodParser 호출

### 메서드 인식 방법
- 접근 제한자(`public`, `private`, `protected`)로 시작하거나
- 수식어(`static`, `final`)로 시작하고
- `(` 토큰이 있는 경우 → 메서드로 판단

## 💡 주요 고려사항

### 현재 구현 범위
- ✅ 메서드 시그니처 파싱
- ✅ 복잡한 타입 처리 (제네릭, 배열, 와일드카드)
- ⏳ 클래스 구조 파싱
- ⏳ 메서드 호출 관계

### 프로토타입 한계
- 주석, 어노테이션 무시
- 람다, 스트림 등 고급 기능 미지원
- 내부 클래스, 익명 클래스 스킵
- 필요시 향후 추가 가능

## 📈 성공 기준

- [x] 메서드 시그니처 정확히 파싱
- [ ] 클래스 정보 완전히 추출
- [ ] 프로젝트 전체 파일 분석
- [ ] 메서드 호출 관계 추적
- [ ] JSON 파일 생성
- [ ] 실제 프로젝트로 검증

## 🔧 개발 환경

- **언어:** Java 17+
- **테스트:** JUnit 5
- **외부 의존성:** 없음 (바닐라 Java)