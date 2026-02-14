package com.nova.anonymousplanet.core.exception.domain.user;

import com.nova.anonymousplanet.core.constant.error.ErrorCode;
import com.nova.anonymousplanet.core.exception.category.ResourceNotFoundException;
import lombok.Getter;
import org.springframework.context.annotation.Profile;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.exception.domain.user
 * fileName : ProfileImageNotFoundException
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
public class ProfileImageNotFoundException extends ResourceNotFoundException {
    public ProfileImageNotFoundException() {
        super(ErrorCode.PROFILE_IMAGE_NOT_FOUND);
    }
}
