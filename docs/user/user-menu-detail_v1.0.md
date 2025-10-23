# user-menu-detail_v2.0.md
**버전:** 2.0  
**목적:** 사용자 관점의 페이지별 상세 설계(친구모드 → 맞선모드 흐름, 설문 규칙, 프로필/사진 정책, 메시지/예약, 에러/예외 정책 등)  
**작성일:** 2025-09-25

---

## 목차
1. 전반적 UX 원칙
2. 온보딩 & 회원가입 (친구모드 기본가입)
3. 본인 인증(내국인 / 외국인(한국 거주)) 규격
4. 사진 업로드 정책 & AI 검증
5. 프로필 입력(페이지별 상세) — 입력 항목/유효성/도움말
6. 가입 상태와 전환 규칙(state machine)
7. 맞선(유료) 가입 — 추가 항목/보증금/승인 흐름
8. 설문(Survey) — 맞선 회원 전용 규칙(일일 13:00, 최초 1회) 및 예외처리
9. 매칭 결과 화면 — UI/데이터 노출 방식
10. 채팅 / 신고 / 긴급안전 기능
11. 마이페이지(친구모드 vs 맞선모드 차이)
12. 에러/예외 시나리오 & UX 문구
13. QA/검수 체크리스트 (페이지별)
14. 변경 이력

---

## 1) 전반적 UX 원칙
- **진입 장벽 최소화**: 친구모드 기본가입은 필수항목 최소화(사진3장, 기본정보) → 이후 단계에서 점진적 정보 요청(Progressive Disclosure).
- **신뢰성 노출**: 회원 인증 레벨(Verified badge), 보증금/매칭완료 뱃지 등 시각화.
- **명확한 제약표시**: 가입대기 / 기능제한 시 UI에서 명확히 알림(예: "가입대기 상태에서는 채팅 불가").

---

## 2) 온보딩 & 회원가입 (친구모드)
- **흐름:** 언어선택 → 이메일/PW/닉네임 → 본인 인증 → 사진 업로드 → 프로필 입력(3단계) → 가입 완료(가입대기)
- **페이지별 상세**
    - `/onboarding/language`
        - UI: 3개의 버튼(한국어/English/日本語), 선택 시 로컬 저장 + 서버에 preference 저장
    - `/signup/account`
        - 필드: email(필수), password(필수), password_confirm, nickname(필수)
        - 유효성: 이메일 포맷, 비밀번호 규칙(8+), 닉네임 중복 체크
        - microcopy: "서비스 약관·개인정보처리방침에 동의하셔야 가입이 가능합니다."
    - `/signup/verify`
        - 동작: 통신사 PASS 연동(내국인) / 외국인 등록번호 + 통신사 연동(외국인 한국 거주)
        - 실패 처리: 3회 실패 시 고객센터 안내
    - `/signup/photos`
        - 인터페이스: 썸네일 5칸, 드래그로 순서변경, 각 사진에 '얼굴 인식 통과' 표시
        - 제약: 확장자 JPG/PNG, 10MB 이하, 최소 3장 업로드 필요
        - AI 검증: 얼굴 존재 여부, 과다 필터/스티커 검출 → 실패 시 재업로드 요구
    - `/signup/profile/step1` (기본)
        - fields: gender(필수), birthdate(필수), residence_city(필수), activity_areas(최대3)
        - validation: birthdate → 만 19세 이상 체크
    - `/signup/profile/step2` (학력/직업/언어)
        - fields: education(옵션 다중), job_title(옵션), languages (list with level)
    - `/signup/profile/step3` (취미/소개/간단 이상형)
        - hobby tags(최대 10), intro(50~500자 권장), simple_pref(age_range, region)
    - `/signup/complete`
        - 상태: 가입대기(Locked features 안내)
        - 버튼: "메인으로 가기" / "맞선 모드 신청" (비활성화 가능)

---

## 3) 본인 인증 규격
- **내국인**
    - 통신사 PASS 연동(휴대폰 본인인증)
    - 반환값: verified=true/false, phone_number (hashed), provider, timestamp
- **외국인(한국 거주)**
    - 외국인등록증 OCR + 통신사 인증 플로우
    - 보완: OCR 실패 시 수동 검토 요청 가능(관리자 화면에 티켓 생성)
- **인증 레벨**
    - Level 0: 미인증
    - Level 1: 전화번호 인증
    - Level 2: ID OCR 서류 인증
    - Level 3: 운영자 수동 확인(추가 신뢰 레벨)
- **로그**
    - 인증 시도 기록(유저ID, type, result, provider, ip, timestamp)

---

## 4) 사진 업로드 & AI 검증
- **요건**
    - 최소 3장, 최대 5장
    - 1장 이상 얼굴 전면 사진 필수
    - 얼굴이 식별 불가/과도한 필터/타인 얼굴 업로드 시 차단
- **AI 체크 항목**
    - 얼굴 존재 확인 (Face-detection)
    - 복수 얼굴 여부(다른 사람 포함 시 경고)
    - 필터·스티커 과다 여부
    - 동일사진 중복체크 (해시 비교)
- **관리자 도구**
    - 사진 상세보기(원본 다운로드), AI판단 사유 표시, 운영자 수동 승인/반려 버튼
- **오류 메시지 예시**
    - "사진에 얼굴이 인식되지 않습니다. 정면 사진을 다시 업로드해주세요."

---

## 5) 프로필 입력(페이지별 상세) — Validation & Microcopy

### Profile Step1 — Basic Info
- Fields:
    - Nickname (2~12 chars) — 중복검사
    - Gender (Male/Female/Other)
    - Birthdate (YYYY-MM-DD) — 만19세 이상 체크
    - Residence: City/Gu dropdown
    - Activity Areas: up to 3 (autocomplete)
- UX:
    - Title: "당신을 소개해 주세요"
    - Help text: "거주지역은 약속 장소 결정에 사용됩니다."

### Profile Step2 — Education / Job / Languages
- Fields:
    - Education: enum multi-select(HighSchool/College/University/Graduate)
    - Job: occupation category + free text for detail (optional)
    - Company: optional
    - Languages: [Korean, English, Japanese, Chinese, Other] + proficiency (Basic/Conversational/Fluent/Native)
- Validation:
    - Optional fields: 빈값 허용
- Microcopy:
    - "직업/학력 정보는 매칭의 신뢰도를 높입니다."

### Profile Step3 — Hobbies / Intro / Simple Prefs
- Fields:
    - Hobbies: multi-tag selection + custom tag input (max 10)
    - Self-intro: text area (min 50 char 권장)
    - Simple preference: target age range, preferred region (친구모드: 최소값만)
- Microcopy:
    - "진솔한 자기소개가 매칭 확률을 높입니다."

---

## 6) 가입 상태(State Machine)
- 상태 목록:
    - `REGISTERED` (이메일 등록 완료)
    - `VERIFIED` (휴대폰/ID 인증 완료)
    - `JOIN_PENDING` (가입대기 — 운영자 승인 필요)
    - `FRIEND_ACTIVE` (친구모드 활성)
    - `MATCH_PENDING` (맞선모드 가입대기)
    - `MATCH_ACTIVE` (맞선모드 활성)
    - `MATCH_COMPLETE` (맞선 완료 → 맞선 기능 제한/커플 뱃지)
    - `SUSPENDED` (정지)
    - `BANNED` (추방)
    - `WITHDRAWN` (탈퇴)
- 전환 예:
    - REGISTERED → VERIFIED (인증 성공)
    - VERIFIED → JOIN_PENDING (프로필 제출) → FRIEND_ACTIVE (운영자 승인)
    - FRIEND_ACTIVE → MATCH_PENDING (맞선 신청 + 보증금) → MATCH_ACTIVE (운영자 승인)

- **권한/제약**
    - JOIN_PENDING 시: 검색/프로필 열람 가능, 매칭 신청 및 채팅 불가
    - MATCH_ACTIVE 시: 맞선 관련 기능 활성, 친구모드 기능은 계속 사용 가능(정책에 따라 조정)

---

## 7) 맞선(유료) 가입 — 규격
- **요구사항**
    - 기존 친구모드 회원만 신청 가능
    - 보증금(운영자가 설정) 납부 필요(결제 연동)
    - 추가 프로필(상세 이상형/생활습관/학력/직업 인증) 필수
- **승인 프로세스**
    - 납부 확인 → 운영자 검토(신원/사진검증/문항 응답) → 승인/반려
- **결과**
    - 승인 시 `MATCH_ACTIVE` 변환, 포인트 차감 규칙 적용
    - 반려 시 사유 기록 및 사용자 알림

---

## 8) 설문(Survey) 규칙 — 맞선 회원 전용
- **운영 규칙**
    - 대상: MATCH_ACTIVE 회원
    - 주기: 매일 13:00 시작(24시간 유효)
    - 제한: 해당 24시간 내 **메인 최초 접속 시 1회만** 참여 가능(제출 후 수정 불가)
    - 문항수: 기본 10문항(관리자에서 조정 가능)
    - 문항 유형: 객관식(4지선다) 우선, 필요시 5점 척도 또는 주관식(운영자 검토)
- **에지 케이스**
    - 오프라인 상태에서 작성: 로컬 캐시 후 전송 → 최초 접속 규칙 우회 확인 필요(서버 측 timestamp로 검증)
    - 미참여: 해당일 미반영(다음날 재시도)
- **데이터 보관**
    - `survey_response(user_id, question_id, choice_id, timestamp, version)`
    - 버전 관리: 문항 변경 시 기존 응답과 신뢰성 보장 위해 version 부여

---

## 9) 매칭 결과 화면 — 상세 노출 규격
- **요약 카드(리스트)**
    - 상대 닉네임, 나이, 지역, 공통 취미 수, 매칭 확률(%)
    - 액션: 프로필 보기 / 관심(하트) / 거절
- **상세 프로필 화면**
    - 상단: 사진 갤러리 + 신뢰 뱃지(Verified/Matched)
    - 우측/하단: "왜 맞는가" 근거(자동 생성)
        - MBTI 호환(점수), 설문 일치(문항수/점수), 공통취미 수, 나이/지역 적합 여부
    - 하단 액션:
        - 매칭 요청(포인트 차감) 또는 채팅 요청(권한/상태 확인)
- **매칭 확률 표시**
    - 표시 방식: `매칭 확률: 82%` (큰 숫자) + 근거 토글(펼치면 항목별 점수)
    - 주의문구: "이 확률은 예상치이며 참고용입니다."

---

## 10) 채팅 / 신고 / 긴급안전 기능
- **채팅**
    - 권한: 매칭된 상대 또는 친구요청 수락한 상대만 가능
    - 기능: 텍스트, 이미지 전송, 위치 공유(옵션)
    - 포인트: 메시지 전송 시 포인트 차감(정책에 따라)
- **신고**
    - 신고 유형: 스팸/비속어/위협/가짜 프로필/성희롱 등
    - 신고 시 첨부: 스크린샷 권장
    - 처리: Manager 1차 처리 → 필요한 경우 Admin escalated
- **긴급안전**
    - 실시간 위치 공유(옵션), 긴급호출 버튼(관리자/콜센터에 알림)
    - 오프라인 만남 예약 시 안전옵션: 3자 동석(직원) 선택 가능(유료/무료 설정)

---

## 11) 마이페이지 (친구모드 vs 맞선모드)
- **공통**
    - 프로필 편집, 사진 관리, 포인트 내역, 알림설정, 계정설정
- **친구모드 전용**
    - 간단 이상형 설정(나이/지역), 친구 활동 로그
- **맞선모드 전용**
    - 상세 이상형 편집, 보증금 현황, 설문 이력, matches(대기/확정/완료) 관리

---

## 12) 에러/예외 시나리오 & UX 문구 예시
- 인증 실패: "본인 인증에 실패했습니다. 고객센터에 문의해주세요."
- 사진 검증 실패: "사진 규격을 확인해주세요. 얼굴 인식이 안됩니다."
- 설문 미응답: "오늘의 설문에 참여하지 않으셨습니다. 다음 기회에 참여해주세요."
- 매칭 포인트 부족: "포인트가 부족합니다. 충전 페이지로 이동하시겠습니까?"

---

## 13) QA / 검수 체크리스트 (페이지별 발췌)
- **회원가입**
    - 이메일 중복 체크 정상 동작
    - 전화 인증 성공/실패 케이스 테스트
    - 사진 3장 미만 차단 확인
- **프로필**
    - 필드별 유효성, maxlength/minlength 체크
    - 활동지역 3개 제한 확인
- **설문**
    - 최초 메인 접속 시 1회만 뜨는지 확인(서버 타임스탬프 기준)
    - 오프라인 작성 후 전송 시 timestamp 비교로 중복 회피
- **매칭 결과**
    - 확률 표시 및 근거 토글 동작 검증

---

## 14) 변경 이력
- v2.0: 상세 규격 보강(페이지별 필드, 상태머신, 설문 규칙, 로그 명세) - 2025-09-25

---
