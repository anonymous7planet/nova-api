package com.nova.anonymousplanet.messaging.model;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.model.sender
 * fileName : EmailAttachment
 * author : Jinhong Min
 * date : 2025-11-17
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-11-17      Jinhong Min      최초 생성
 * ==============================================
 */

public record EmailAttachment (
   String fileName,
   byte[] data,
   String contentType
) {

}
