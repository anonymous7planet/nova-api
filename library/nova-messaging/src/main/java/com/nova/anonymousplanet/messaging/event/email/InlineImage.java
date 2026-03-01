package com.nova.anonymousplanet.messaging.event.email;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.model.sender
 * fileName : InlineImage
 * author : Jinhong Min
 * date : 2025-11-17
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-17      Jinhong Min      최초 생성
 * ==============================================
 */
public record InlineImage(
    String fileName,    // 확장자 포함 파일명(ex) logo.png)
    String contentId    // 콘텐트 아이디 (ex) logo)
) {
}
