# Anonymous Planet - Nova

Nova는 **Anonymous Planet** 프로젝트의 백엔드 서버 아키텍처입니다.  
Spring Boot 3.4.4, Spring Cloud 2024.0.1, Java 21을 기반으로 MSA 구조로 설계되었습니다.

## 📦 프로젝트 구조

```
nova/
├── common/
│   └── nova-common              # 공통 라이브러리 (Exception, 상수, Security 설정 등)
├── infrastructure/
│   ├── nova-config-server       # Spring Cloud Config Server
│   ├── nova-discovery-server    # Eureka Discovery Server
│   └── nova-gateway-server      # Gateway (Spring Cloud Gateway + JWT 인증)
├── services/
│   └── nova-auth-service        # 인증 서비스 (로그인, 회원가입, JWT 발급/재발급)
└── build.gradle                 # 루트 빌드 설정
```

### 📦 패키지 구조 (각 서비스 공통)

```
com.nova.anonymousplanet.{service}
├── domain         // Entity, Enum 등 핵심 도메인 모델
├── application    // UseCase, Service, Command/Query 핸들링
├── interfaces     // Controller, DTO, 외부 요청 처리
├── infrastructure // Repository, 외부 시스템 연동 (JPA, Kafka 등)
```

---

## 🧱 기술 스택

- Java 21
- Spring Boot 3.4.4
- Spring Cloud 2024.0.1
- Gradle (Groovy DSL)
- Spring Cloud Config, Eureka, Gateway
- JWT, Spring Security
- Docker (Redis 등 인프라 구성 예정)

## 🔐 인증

`nova-auth-service`는 다음 기능을 담당합니다:

- 회원가입
- 로그인
- JWT 토큰 발급 및 재발급

인증이 필요한 서비스는 JWT를 헤더로 전달받아 처리합니다.  
인증 관련된 공통 로직은 `nova-common`에 정의됩니다.

## 📁 공통 모듈

- `nova-common`: 모든 서비스에서 사용할 수 있는 공통 유틸, 예외 처리, 상수, Security 설정 등을 포함합니다.
- DTO, BaseEntity, ErrorCode, ExceptionHandler 등도 여기에 위치합니다.

## 🔧 빌드/설정

루트 `build.gradle`에서 공통 설정과 Spring Boot / Spring Cloud BOM을 관리합니다.  
공통 Java 버전, BOM 버전, 의존성 관리, 인코딩 설정 등을 적용합니다.

---

> ✅ MSA 구조를 깔끔하게 정리하고, 각 역할에 맞게 모듈을 나눠 유지보수성과 확장성을 고려한 프로젝트입니다.