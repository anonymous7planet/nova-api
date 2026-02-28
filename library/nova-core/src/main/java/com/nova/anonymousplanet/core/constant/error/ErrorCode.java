package com.nova.anonymousplanet.core.constant.error;

import com.nova.anonymousplanet.core.constant.DisplayTypeCode;
import org.springframework.http.HttpStatus;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.constant.error
 * fileName : ErrorCode
 * author : Jinhong Min
 * date : 2026-02-27
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-27      Jinhong Min      최초 생성
 * ==============================================
 */
public interface ErrorCode {
    HttpStatus getStatus();
    String getCode();          // 그룹 + 상세 코드 (예: G001)
    String getFullCode();      // 접두사 포함 전체 코드 (예: COM-G-001)
    String getMessage();       // 기본 사용자 메시지
    DisplayTypeCode getDisplayType(); // UI 출력 방식
    String getName();
}
