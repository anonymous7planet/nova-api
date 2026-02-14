package com.nova.anonymousplanet.core.exception.domain.user;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.category.BusinessException;
import lombok.Getter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.domain.user
 * fileName : UserAccountLockedException
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
public class UserAccountLockedException extends BusinessException {

    public UserAccountLockedException() {
        super(ErrorCode.USER_INACTIVE);
    }
}
