package com.nova.anonymousplanet.core.exception.domain.user;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.category.InvalidRequestException;
import lombok.Getter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.domain.user
 * fileName : DuplicateEmailException
 * author : Jinhong Min
 * date : 2026-02-14
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-14      Jinhong Min      최초 생성
 * ==============================================
 */
@Getter
public class DuplicateEmailException extends InvalidRequestException {

    public DuplicateEmailException() {
        super(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
    }
}
