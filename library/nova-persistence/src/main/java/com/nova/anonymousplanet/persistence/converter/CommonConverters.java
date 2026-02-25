package com.nova.anonymousplanet.persistence.converter;

import com.nova.anonymousplanet.core.constant.*;
import jakarta.persistence.Converter;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.persistence.converter
 * fileName : CommonConverters
 * author : Jinhong Min
 * date : 2026-02-25
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-02-25      Jinhong Min      최초 생성
 * ==============================================
 */
public class CommonConverters {

    @Converter(autoApply = true)
    public static class YesNoCodeConverter extends BaseEnumConverter<YesNoCode, String> {
        public YesNoCodeConverter() {
            super(YesNoCode.class);
        }
    }

    @Converter(autoApply = true)
    public static class UserStatusCodeConverter extends BaseEnumConverter<UserStatusCode, String> {
        public UserStatusCodeConverter() {
            super(UserStatusCode.class);
        }
    }

    @Converter(autoApply = true)
    public static class RoleCodeConverter extends BaseEnumConverter<UserRoleCode, String> {
        public RoleCodeConverter() {
            super(UserRoleCode.class);
        }
    }

    @Converter(autoApply = true)
    public static class ServiceModeCodeConverter extends BaseEnumConverter<ServiceModeCode, String> {
        public ServiceModeCodeConverter() {
            super(ServiceModeCode.class);
        }
    }

    @Converter(autoApply = true)
    public static class ReligionCodeConverter extends BaseEnumConverter<ReligionCode, String> {
        public ReligionCodeConverter() {
            super(ReligionCode.class);
        }
    }

    @Converter(autoApply = true)
    public static class NotificationTypeCodeConverter extends BaseEnumConverter<NotificationTypeCode, String> {
        public NotificationTypeCodeConverter() {
            super(NotificationTypeCode.class);
        }
    }

    @Converter(autoApply = true)
    public static class NotificationStatusCodeConverter extends BaseEnumConverter<NotificationStatusCode, String> {
        public NotificationStatusCodeConverter() {
            super(NotificationStatusCode.class);
        }
    }

    @Converter(autoApply = true)
    public static class MbtiCodeConverter extends BaseEnumConverter<MbtiCode, String> {
        protected MbtiCodeConverter() {
            super(MbtiCode.class);
        }
    }

    @Converter(autoApply = true)
    public static class LoginProviderCodeConverter extends BaseEnumConverter<LoginProviderCode, String> {
        protected LoginProviderCodeConverter() {
            super(LoginProviderCode.class);
        }
    }

    @Converter(autoApply = true)
    public static class LanguageCodeConverter extends BaseEnumConverter<LanguageCode, String> {
        public LanguageCodeConverter() {
            super(LanguageCode.class);
        }
    }

    @Converter(autoApply = true)
    public static class JobCategoryCodeConverter extends BaseEnumConverter<JobCategoryCode, String> {
        public JobCategoryCodeConverter() {
            super(JobCategoryCode.class);
        }
    }

    @Converter(autoApply = true)
    public static class GenderCodeConverter extends BaseEnumConverter<GenderCode, String> {
        public GenderCodeConverter() {
            super(GenderCode.class);
        }
    }

    @Converter(autoApply = true)
    public static class EduCationLevelCodeConverter extends BaseEnumConverter<EducationLevelCode, String> {
        public EduCationLevelCodeConverter() {
            super(EducationLevelCode.class);
        }
    }

    @Converter(autoApply = true)
    public static class BloodTypeCodeConverter extends BaseEnumConverter<BloodTypeCode, String> {
        public BloodTypeCodeConverter() {
            super(BloodTypeCode.class);
        }
    }

    @Converter(autoApply = true)
    public static class AdminRoleConverter extends BaseEnumConverter<AdminRoleCode, String> {
        public AdminRoleConverter() {
            super(AdminRoleCode.class);
        }
    }
}
