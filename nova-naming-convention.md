
# 🌌 Nova 프로젝트 네이밍 컨벤션

Nova 프로젝트에서 사용하는 네이밍 컨벤션을 정리한 문서입니다. 각 이름은 명확성, 일관성, 유지보수 편의성을 기준으로 설정되었습니다.

---

## 📦 패키지 및 클래스 네이밍

| 유형          | 예시                              | 설명 |
|---------------|-----------------------------------|------|
| 패키지명       | `com.nova.anonymousplanet.auth`  | 소문자, 도메인 구조 기반으로 구성 |
| 하위 패키지    | `domain.member`, `application.token` | 도메인 중심 구조(Hexagonal Architecture)로 분리 |
| 엔티티 클래스 | `MemberEntity`                    | `Entity` 접미사 명시적으로 사용 |
| DTO 클래스     | `LoginRequestDto`, `MemberResponseDto` | `Dto` 접미사 사용 |
| 열거형 클래스 | `MemberStatusCode`                | `Code` 접미사 사용 |
| 예외 클래스   | `MemberNotFoundException`         | 의미를 명확하게 표현 |
| 유틸 클래스   | `EnumUtils`                       | 복수형 사용. 동작 또는 집합의 의미 강조 |

---

## 🔁 Enum 관련 컨벤션

- 열거형 클래스는 접미사로 `Code`를 사용합니다.
  - 예: `MemberStatusCode`
- 각 Enum 클래스에는 내부에 `AttributeConverter`를 `static class`로 정의합니다.
  - 예: `MemberStatusCodeConverter`
- 예외는 공통 베이스 예외를 사용하여 처리하며, 커스텀 예외를 정의할 수 있습니다.
- `EnumUtils`와 같은 유틸 클래스는 공통적인 Enum 관련 기능을 처리합니다.

---

## 🧱 Hexagonal Architecture 기반 구성

```
nova-auth-service
└── src/main/java/com/nova/anonymousplanet/auth
    ├── domain          # Entity, VO, Enum 등 핵심 도메인 로직
    │   └── member
    ├── application     # UseCase 및 서비스 로직
    │   └── member
    ├── interfaces      # Controller 및 외부 요청 처리
    │   └── rest
    └── infrastructure  # DB, 외부 API 연동 등
        └── member
```

---

## ✨ 클래스 및 패키지 설명용 `package-info.java`

각 패키지에는 `package-info.java` 파일을 생성하고 이모지와 함께 간결한 설명을 작성합니다. 예시:

```java
/**
 * 🧠 Member 도메인 엔티티 및 코드 정의 패키지입니다.
 */
package com.nova.anonymousplanet.auth.domain.member;
```

---

## 📌 기타 기준

| 구분        | 기준 |
|-------------|------|
| Builder 패턴 | 생성자 대신 명확한 빌더 제공. `@Builder` 사용 권장 |
| VO vs DTO   | VO는 도메인 모델 내부 표현용, DTO는 계층 간 전달 목적 |
| Command/Query 구분 | CUD → Command, R(조회) → Query |

---

✅ **이 문서는 유지보수 시 지속적으로 업데이트됩니다.**
