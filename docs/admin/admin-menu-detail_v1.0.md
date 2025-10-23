# admin-menu-detail_v1.0.md
**버전:** 1.0  
**목적:** 관리자 운영을 위한 화면/기능/권한/로그/데이터 표현 방식 등 상세 규격 문서  
**작성일:** 2025-09-25

---

## 목차
1. 개요 / 권한 모델
2. 로그인 / 보안
3. 대시보드(Dashboard) 상세 위젯 & 그래프 규격
4. 회원 관리(Member Management) — 목록 / 상세 / 동작 규격
5. 매칭 관리(Matching Management) — 자동/수동/문항 편집
6. 포인트 & 결제 관리(Point & Payment)
7. 콘텐츠 관리(Content)
8. 신고/고객센터(Report / Support)
9. 시스템 관리(System)
10. 로그 관리(Log)
11. 감사·운영 워크플로우 샘플
12. 운영(관리자) UI/데이터 예시 & 검수 기준
13. 변경 이력

---

## 1) 개요 / 권한 모델
- 관리자 권한(4단계)
    - **SuperAdmin (최고관리자)**: 모든 권한(권한부여/정책변경/수동매칭/시스템설정).
    - **Admin (관리자)**: 회원/매칭/포인트/콘텐츠/신고 처리 가능. 단, 수동매칭(최종확정)과 시스템 핵심설정 일부 제한.
    - **Manager (매니저)**: 회원 조회/승인, 신고 1차 처리, 고객센터 응대. 정책 변경 불가.
    - **Analyst (분석가, 선택)**: 대시보드/통계 조회 전용(편집/운영 불가).

- **권한 매핑 표 (주요 메뉴별 권한)**
  | 메뉴 / 액션 | SuperAdmin | Admin | Manager | Analyst |
  |-------------|------------:|------:|--------:|--------:|
  | Dashboard 보기 | O | O | O | O |
  | 회원 목록 조회 | O | O | O | O |
  | 회원 수정(프로필/상태) | O | O | △(제한) | X |
  | 맞선 가입 승인 | O | O | △(추천만) | X |
  | 수동 매칭 실행 | O | △(추천/제안) | X | X |
  | 포인트 정책 변경 | O | O | X | X |
  | 문항/매트릭스 편집 | O | O | X | X |
  | 시스템 설정(버전/인증) | O | X | X | X |
  | 로그(감사) 조회 | O | O | O | O |
    - `△(제한)` : 일부 기능(예: 제안/임시저장)은 가능하나 최종확정 권한 없음.

---

## 2) 로그인 / 보안
- 로그인 방식: 관리자 전용 계정(이메일/비밀번호) + 2단계 인증(선택) 적용 권장.
- 비밀번호 규칙: 12자 이상 권장, 대/소문자/숫자/특수문자 권장.
- 세션 정책: 비활성 30분 자동 로그아웃.
- 로그인 실패 시도: 5회 실패 → 계정 잠금(관리자 해제 필요).
- 로그인 로그: `admin_login_log` 테이블에 저장(관리자ID, IP, user_agent, timestamp, success/failed reason).

---

## 3) 대시보드(Dashboard) — 위젯 규격
> 목표: 운영자가 '오늘 상태'를 한눈에 파악하고, 이상 징후(신고 급증, 결제 이상 등)를 빠르게 인지.

### 위젯 구성(권장 3행 레이아웃)
1. **KPI 카드(상단 4개)** — 간단 수치
    - 신규 가입(오늘) : 숫자 + 전일 대비 증감(▲/▼)
    - 활성 사용자(7일 MA) : 숫자
    - 매칭 성사(오늘) : 건수
    - 미처리 신고 건수: 건수(클릭 → 신고관리)

2. **회원 지표**
    - **Line Chart**: 일별 가입자 추이(기간: 7/30/90일)
        - 데이터: `date, new_users_count`
    - **Donut Chart**: 모드별 회원 비율 (친구모드 / 맞선모드 / 기타)
    - **Table**: 최근 가입 상위 10명(닉네임, 가입일, 지역, 인증여부)

3. **매칭 지표**
    - **Bar Chart (일별)**: 매칭 시도 vs 매칭 성공 (기간: 7/30일)
    - **Heatmap (시간대)**: 시간대별 매칭 시도 분포 (0-23h x 요일)
    - **Distribution (Histogram)**: 매칭 점수 분포(0-100) — 버킷 10

4. **결제/포인트**
    - **Area Chart**: 일/주/월 결제 금액 변화
    - **Donut**: 결제 수단 비율 (카드/간편/기타)
    - **Table**: 최근 결제 10건

5. **신고 / 고객센터**
    - 미처리 신고 카드 (급한 순서 표기)
    - 신규 문의 카드

6. **시스템 알림**
    - 앱 버전 불일치 경고(지원종료 기기)
    - 서버 에러 증가(최근 1시간 대비)

### UX/동작
- 시간 범위 선택(Last7/30/90/Custom) → 각 차트 동시 반영.
- 차트 툴팁: 수치 + 전일비율 + 증감률.
- 데이터 뷰: CSV 다운로드 버튼(차트별).

---

## 4) 회원 관리 — 상세 규격

### 4.1 회원 목록 화면
- **URL /admin/members**
- 컬럼(기본): MemberID, Nickname, RealName*(masked)*, Mode(Friend/Match), Status(Active/Waiting/Banned/Withdrawn), Age, Gender, Region, LastLogin, PointsBalance, VerificationLevel, Actions
- 필터: 가입일(From/To), 상태, 성별, 연령대, 지역, 인증여부, 신고이력 유무, 보증금 납부 여부
- 정렬: 가입일, 최근접속, 포인트 보유, 신고건수
- bulk action: CSV export, bulk message(send push/email), bulk suspend/reactivate (권한 제한)

### 4.2 회원 상세 보기
- **탭 구조**: Overview | Profile | Activity | Matching | Payments | Reports | Audit Log
    - **Overview (기본 정보)**
        - MemberID, Nickname, RealName(관리자 전용 마스크 해제 가능 로그 기록), Email, Phone(maybe masked), Gender, Birthdate, Age, JoinDate, Mode(친구/맞선), Status(가입대기/활성/탈퇴/추방), Verification Level(0:None/1:Phone/2:ID OCR/3:Operator Verified)
        - Profile Completeness (%) — 계산 규칙(사진 수, 자기소개, 취미 등)
        - Badges (e.g., Verified, CoupleBadge)
    - **Profile (프로필 정보)**
        - Photos (썸네일, 원본 다운로드 버튼, AI face-check 결과 표시)
        - 자기소개, 취미 태그, MBTI, 언어 능력, 활동지역(3곳), 학력/직업/기타
        - 이상형(맞선모드): age range, region, job/education prefs, lifestyle 체크리스트
    - **Activity (활동 내역)**
        - 최근 로그인 리스트 (timestamp + IP region)
        - 매칭 이력 (요청/수락/성사/실패) — 각 항목 클릭 시 matching detail으로 이동
        - 채팅 사용량(총 메시지 수, 신고된 메시지 수)
        - 설문 응답 히스토리(날짜별 요약)
    - **Matching**
        - 현재 매칭 상태(대기/활동/완료)
        - 과거 매칭 상대 리스트(상대 닉네임 링크, 점수, 매칭일)
    - **Payments**
        - 보증금 납부 내역(금액, 납부일, 반환상태)
        - 포인트 충전/차감 내역
    - **Reports/Warnings**
        - 신고 이력 (신고자, 사유, 첨부파일, 처리결과)
        - 제재 이력(경고/정지/추방)
    - **Audit Log**
        - 관리자 행위 로그(누가 언제 무엇을 수정했는지: field-level diff 저장)
        - 예: `2025-09-24 10:12 - Admin[alice] changed verification_level from 1 -> 2`

### 4.3 행동(액션) 버튼
- Approve / Reject (가입대기 처리) — 권한 필요
- Suspend / Unsuspend (정지)
- Ban (추방) — 로그/근거 첨부 필수
- Refund Deposit (보증금 반환 처리) — 결제 연동
- Export Profile (CSV/PDF)
- Add Admin Note (운영 메모) — 내부전용, 공개불가

---

## 5) 매칭 관리 — 상세

### 5.1 자동 매칭 설정
- UI: 가중치 슬라이더(각 항목에 % 할당). 합산 100%로 강제. 기본값 제안: MBTI 20 / Survey 40 / Hobby 20 / Region 10 / Other 10.
- 매칭 임계값(Threshold) 설정: 추천 노출 최소 점수(예: 60)
- 재계산 정책: 실시간 / 예약(예: 1시간 단위) 선택 가능
- 저장 시 변경 이력 기록(누가, 언제, 이전값)

### 5.2 매칭 문항 관리 (Question Management)
- 문항 목록: QID, 제목, 유형, 상태(활성/비활성), version
- 문항 편집 화면:
    - 타입: Single-choice(4), Multi-choice, 5-point-scale, Free-text
    - Choice management: 각 choice에 ID 부여
    - **Compatibility Matrix Editor**: choice x choice 표 형태로 가중치 입력 가능(스프레드시트 느낌). 값 범위: -10 ~ +50
    - Preview: "User A 선택 X vs User B 선택 Y → +Z 점" 미리 보기
- 버전 관리: 문항 수정 시 새로운 version 생성, 과거 응답과의 호환성 표기

### 5.3 수동 매칭 (SuperAdmin 전용)
- 워크플로우:
    1. SuperAdmin 검색(회원A) → 프로필 확인
    2. 후보군(자동 추천 또는 수동 검색) 표시
    3. 후보 선택(최대 N명) → "Propose Match" (옵션: 자동 확정 또는 양측 수락 필요)
    4. 알림 발송(회원A/B에게)
    5. 매칭 확정 시: 상태 변경, 보증금/포인트 처리, 오프라인 예약 안내
- 감사 기록: 누가 어떤 근거로 매칭했는지 텍스트 기록 필수

---

## 6) 포인트 & 결제 관리 상세
- **포인트 정책 화면**
    - 환율 설정 (예: 1,000 KRW = 1,000P)
    - 차감 규칙 (profile_view=200P, message=100P, match_request=500P 등) — 관리자 편집 가능
- **결제 거래 테이블**
    - 항목: txn_id, user_id, amount, payment_method, status, timestamp, refund_status
- **정산 리포트**
    - 기간 선택/수수료 계산/환불 항목 포함
- **수동 작업**
    - 관리자 권한으로 포인트 지급/회수(사유 기입 필수)
- **감사**
    - 모든 포인트 조작은 감사 로그 저장

---

## 7) 콘텐츠 관리
- 배너: 위치(앱 런치/홈/프로필상단), 노출기간, 우선순위, 링크(내부/외부)
- 이벤트: 캠페인 유형(가입 보너스/첫충전 보너스/프로모션), 쿠폰 발급 관리
- 공지사항: 타겟 지정(전체/지역/모드별), 노출 예약

---

## 8) 신고/고객센터 상세
- 신고 접수 화면:
    - 목록: 신고ID, 신고대상(회원/콘텐츠), 유형, 첨부파일(스크린샷), 접수일, 상태, 담당자
    - 신고 상세: 첨부파일 열람, 증빙 다운로드, 처리 버튼(경고/포인트차감/정지/추방)
- 고객센터(Ticket) 워크플로우:
    - 티켓 생성 → 담당자 배정 → 답변 → 완료
    - SLA 표기(예: 24시간 내 1차 답변 목표)

---

## 9) 시스템 관리
- 관리자 계정 관리(생성/삭제/권한부여)
- 앱 버전 관리: 버전코드/강제업데이트 플래그/지원종료일
- 인증 모듈 설정: PASS/KCB/외국인 OCR 연동 설정 화면(키·엔드포인트)
- 환경 설정: 이메일 템플릿, SMS 템플릿, 긴급 공지 메시지

---

## 10) 로그 관리(감사 시스템) — 세부 규격 (신규 추가)
- **로그 종류 & 항목**
    1. **Admin Action Log (관리자 활동 로그)**
        - 필드: log_id, admin_id, action_type (e.g., MEMBER_UPDATE, MATCH_MANUAL), target_type, target_id, details(diff), timestamp, ip_address
    2. **System Event Log**
        - 필드: event_id, service, level(INFO/WARN/ERROR), message, meta, timestamp
    3. **User Activity Log**
        - 필드: log_id, user_id, action_type(LOGIN, MATCH_REQUEST, MESSAGE_SEND, SURVEY_SUBMIT, PAYMENT), metadata, timestamp
- **조회/필터**
    - 기간, 관리자ID, action_type, 대상ID로 필터 가능
    - 다운로드(CSV), 보존기간 설정(예: 1년 기본 보존, GDPR/로컬 규제 고려)
- **보안**
    - 민감정보(실명/전화번호)는 로그에 마스킹, 필요시 관리자 승인 후 열람 가능
- **감사 프로세스**
    - 중요 변경(권한 변경/금액 환불/추방)은 2인 승인 로그(기록 필수)

---

## 11) 운영 워크플로우 샘플
- **가입 승인 프로세스**
    - 회원가입 → 자동 인증(전화) 통과 → 상태 `가입대기` → Manager가 프로필 검토 → 승인/반려 → 승인 시 `친구모드 활성` (로그)
- **맞선 가입/매칭 프로세스**
    - 맞선 신청 → 보증금 납부 확인 → 운영자(관리자)가 상세정보/신원확인 검토 → 승인 → 매칭 대상자 자동추천(또는 수동 매칭) → 오프라인 예약(관리자 연동)
- **신고 처리 프로세스**
    - 신고 접수 → 1차 처리(Manager) → 조치(경고/포인트차감/정지) → 필요시 Admin escalated(추방)

---

## 12) UI / 데이터 예시 & 검수 기준
- 각 화면은 반응형으로 설계(데스크탑 우선).
- 차트는 D3/Chart.js 등 사용 권장.
- 검수 체크리스트(예: 회원 상세)
    - 필드 표시 일치(컬럼명/형식)
    - 민감정보 접근/마스킹 규칙 검증
    - 로그 남기기(모든 수정 액션)
    - 승인/거절 버튼 권한 확인
- 성능 SLA: 회원 목록 페이지(검색 포함) 200ms 내 응답 목표(검색 쿼리 캐시, 인덱스 필요).

---

## 13) 변경 이력
- v1.0: 기본 상세 규격 작성(2025-09-25)

---