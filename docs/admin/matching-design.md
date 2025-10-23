# Admin - Matching Design (매칭 알고리즘 상세 설계)

## 1. 목적
- 관리자/개발자가 알고리즘 동작과 가중치 조정을 이해하고 운영할 수 있도록 기술적 설명을 제공.

## 2. 입력 데이터
- Profile fields: age, sex, regions[], hobbies[], mbti, languages[], education[], job
- Survey answers: question_id -> choice_id
- System fields: last_active_at, status (friend/union/complete), photo_count, verification_level

## 3. 점수 구성 (가중치 예시)
- MBTI_compat (0 ~ 30)
- Survey_score (0 ~ 40)
- Hobby_score (0 ~ 15)
- Region_score (0 ~ 10)
- Other (education/job) (0 ~ 5)
- 합계 정규화 → 0 ~ 100

## 4. MBTI 호환 계산
- MBTI compatibility 표(16x16) 유지(관리자 편집 가능)
- 값 예: ENFP- INFJ = 20, ENFP- ISTJ = 12
- 계산: MBTI_compat = table[mbtiA][mbtiB]

## 5. Survey compatibility 계산
- 각 Q에 대해 선택지 간 호환점수 matrix(예: 4x4)
- 두 회원의 선택을 비교해 해당 matrix value를 불러와 합산
- 문항 총합을 문항수 및 가중치에 따라 정규화 (예: (sum / max_possible)*40)

## 6. Hobby/Interest 계산
- 교집합 개수 * 가중치_per_hobby (예: 2점씩)
- 만약 취미 태그가 다수라면 TF-IDF 유사도(확장 가능)

## 7. Region 계산
- activity_region 교집합 여부:
    - 동일 구(=행정구) → +10
    - 동일 시(다른 구) → +6
    - 수도권 등 근접 지역 → +3
    - 멀리 있음 → 0

## 8. 정상화(Normalization)
- 각 파트 점수는 파트별 최대값으로 나눈 후 파트 가중치를 곱해 합산
- 예시:
    - mbti_score_norm = MBTI_compat / MBTI_max * 30
    - survey_score_norm = survey_sum / survey_max * 40
    - total = mbti_score_norm + survey_score_norm + ...

## 9. 최종 출력
- total_score (0~100) → UI에 "% 매칭확률"로 노출
- 상세 근거 제공: 각 파트별 점수와 "왜 맞는지" 항목 나열

## 10. 샘플 계산 (가상)
- A vs B:
    - MBTI_compat raw = 18 (MBTI_max 30) → 18/30 * 30 = 18
    - Survey raw sum = 28 (max 35) → 28/35 * 40 = 32
    - Hobby common = 2 → 2*2 = 4 (max 10 => 정규화해서 반영)
    - Region = same 구 → 10
    - 기타 = 2
    - total = 18 + 32 + 4 + 10 + 2 = 66 → "매칭확률: 66%"

## 11. 추가 운영 규칙
- 추천 cutoff: total >= 60 => 추천 리스트 노출
- 운영자가 가중치 실험(AB test) 후 정책 변경 가능
- 수동 매칭 우선권: superadmin 수동 매칭은 자동 점수 무시 가능하되 기록 남김

## 12. 확장 포인트(향후)
- 머신러닝 기반 re-ranking (협업필터링 + content-based)
- 피드백 루프: 매칭 결과(성사/신고/후기) → 가중치 자동 튜닝
