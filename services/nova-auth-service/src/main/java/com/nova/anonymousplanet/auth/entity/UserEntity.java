package com.nova.anonymousplanet.auth.entity;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.domain.user
  fileName : UserEntity
  author : Jinhong Min
  date : 2025-10-27
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-10-27      Jinhong Min      최초 생성
  ==============================================
 */

import com.nova.anonymousplanet.auth.dto.v1.command.UserAuthCommand;
import com.nova.anonymousplanet.core.constant.*;
import com.nova.anonymousplanet.persistence.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_user", indexes = {
        @Index(name = "idx_user_uuid", columnList = "user_uuid"),
        @Index(name = "idx_user_email_hash", columnList = "email_hash"), // 중복 검사 및 조회를 위해서 index추가
        @Index(name = "idx_user_phone_hash", columnList = "phone_number_hash"), // 중복 검사 및 조회를 위해서 index추가
        @Index(name = "idx_user_last_login", columnList = "last_login_at"), // 활동 유저 우선 조회를 위해서 index추가
        @Index(name = "idx_user_status", columnList = "status_code")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UserEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Comment("외부 노출용 고유 식별값")
    @Column(name = "user_uuid", nullable = false, unique = true, updatable = false, length = 40)
    private String uuid;

    @Comment("이메일 해시 (검색/중복체크용)")
    @Column(name = "email_hash", nullable = false, unique = true, length = 100)
    private String emailHash;

    @Comment("전화번호 해시 (중복가입 방지용)")
    @Column(name = "phone_number_hash", nullable = false, unique = true, length = 100)
    private String phoneNumberHash;

    @Comment("전화번호 인증 번호 해시 (중복가입 방지용)")
    @Column(name = "ci_hash", nullable = false, unique = true, length = 100)
    private String ciHash;

    @Comment("비밀번호 (BCrypt 암호화, 소셜로그인시 null 가능)")
    @Column(name = "password", length = 300)
    private String password;

    @Comment("로그인 제공자 (LOCAL, KAKAO, APPLE 등)")
    @Column(name = "login_provider", nullable = false, length = 10)
    private LoginProviderCode loginProvider; // 소셜 로그인 확장을 위해 추가

    @Comment("권한 (USER, ADMIN 등)")
    @Column(name = "role_code", nullable = false, length = 20)
    private UserRoleCode role;

    /* --- 회원 상태 및 서비스 모드 --- */
    @Comment("계정 상태 (정상, 대기, 정지 등)")
    // @Convert(converter = UserStatusCode.UserStatusCodeConverter.class)
    @Column(name = "status_code", nullable = false, length = 10)
    private UserStatusCode status;

    @Comment("서비스 이용 단계 (친구모드, 맞선모드 등)")
    @Column(name = "service_mode_code", nullable = false, length = 10)
    private ServiceModeCode serviceMode;

    @Comment("계정 활성화 여부")
    @Column(name = "active_yn", nullable = false, length = 1)
    private YesNoCode isActive;

    /* --- 운영 및 성능용 필드 --- */
    @Comment("누적 신고 횟수")
    @Column(name = "report_count", nullable = false)
    private Integer reportCount;   // 불량 이용자 필터링 용

    @Comment("마지막 로그인 일시")
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Comment("탈퇴 일시")
    @Column(name = "withdrawn_at")
    private LocalDateTime withdrawnAt; // 탈퇴 후 데이터 유지 기간 산정용

    /* --- 동의 관련 컬럼 추가 --- */
    @Comment("서비스 이용약관 동의 여부")
    @Column(name = "terms_agreed_yn", nullable = false, length = 1)
    private YesNoCode isTermsAgreed;

    @Comment("개인정보 수집 및 이용 동의 여부")
    @Column(name = "privacy_agreed_yn", nullable = false, length = 1)
    private YesNoCode isPrivacyAgreed;

    @Comment("위치기반 서비스 이용 동의 여부")
    @Column(name = "location_agreed_yn", nullable = false, length = 1)
    private YesNoCode isLocationAgreed;

    @Comment("마케팅 활용 동의 여부")
    @Column(name = "marketing_agreed_yn", nullable = false, length = 1)
    private YesNoCode isMarketingAgreed;

    // --- 연관 관계 ---
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserPrivacyEntity privacy;

    /** 프로필 1:1 */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfileEntity profile;

    /** 프로필 이미지 다수 (별도 Entity) */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProfileImageEntity> profileImages = new ArrayList<>();

    /** 메인 프로필 이미지 (OneToOne - FK로 가장 무결성 높음) */
    @Comment("대표 프로필 이미지 ID")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_profile_image_id", nullable = true) // null 허용으로 변경
    private UserProfileImageEntity mainImage;

    /** 자기소개 다수(별도 Entity) */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserIntroductionEntity> introductions = new ArrayList<>();


    /**
     * 일반 사용자 생성 메서드
     * 모든 주요 필드를 지정해 객체를 생성합니다.
     */
    public static UserEntity createUser(
            String uuid,
            String emailHash,
            String phoneNumberHash,
            String ciHash,
            String encodedPassword,
            LoginProviderCode loginProvider,
            UserRoleCode role,
            YesNoCode isTermsAgreed,
            YesNoCode isPrivacyAgreed,
            YesNoCode isLocationAgreed,
            YesNoCode isMarketingAgreed
    ) {
        return UserEntity.builder()
                .uuid(uuid)                                 // 고유 식별자(UUID)
                .emailHash(emailHash)                       // 이메일 검색용 해시
                .phoneNumberHash(phoneNumberHash)           // 전화번호 검색용 해시
                .ciHash(ciHash)                             // CI 검색용 해시
                .password(encodedPassword)                  // 암호화된 비밀번호
                .loginProvider(loginProvider)               // 가입 경로 (LOCAL, KAKAO 등)
                .role(role)                                 // 기본 권한: USER
                .status(UserStatusCode.PENDING)             // 기본 상태: 가입대기
                .serviceMode(ServiceModeCode.NONE)          // 서비스모드: 미승인 유저
                .isActive(YesNoCode.NO)                     // 최초 회원가입시 계정 비활성화
                .reportCount(0)                             // 초기 신고 횟수: 0
                .isTermsAgreed(isTermsAgreed)               // 서비스 이용약관 동의
                .isPrivacyAgreed(isPrivacyAgreed)           // 개인정보 동의
                .isLocationAgreed(isLocationAgreed)         // 위치기반 서비스 동의 (소개팅 핵심)
                .isMarketingAgreed(isMarketingAgreed)       // 마케팅 동의
                .build();
    }

    /**
     * 소셜 로그인 사용자 생성 (비밀번호가 없는 경우를 위한 오버로딩)
     */
    public static UserEntity createSocialUser(
            String uuid,
            String emailHash,
            String phoneNumberHash,
            LoginProviderCode loginProvider,
            UserRoleCode role,
            YesNoCode isLocationAgreed
    ) {
        return UserEntity.createUser(
                uuid,
                emailHash,
                phoneNumberHash,
                null,
                null, // 소셜 유저는 비밀번호 null 가능
                loginProvider,
                role,
                YesNoCode.YES,
                YesNoCode.YES,
                isLocationAgreed,
                YesNoCode.NO
        );
    }


    /**
     * 연관 관계 데이터 설정
     * @param command
     */
    public void registerUserInfo(UserAuthCommand.UserSignupCommand command) {
        // privacyEntity
        this.privacy = UserPrivacyEntity.create(this, command.privacy());
        // profileEntity
        this.profile = UserProfileEntity.create(this, command.profile());
        // introductionEntity
        // ProfileImage
    }



    /**
     * [관리자용] 시스템 관리자에 의한 강제 계정 생성
     */
    public static UserEntity createAdminUser(
            String uuid,
            String emailHash,
            String phoneNumberHash,
            String encodedPassword,
            UserRoleCode role
    ) {
        ServiceModeCode serviceMode = ServiceModeCode.MATCH;
        if(role != UserRoleCode.ADMIN) {
            serviceMode = ServiceModeCode.FRIEND;
        }
        return UserEntity.builder()
                .uuid(uuid)
                .emailHash(emailHash)
                .phoneNumberHash(phoneNumberHash)
                .password(encodedPassword)
                .loginProvider(LoginProviderCode.DANBY)
                .role(role)
                .status(UserStatusCode.NORMAL)
                .serviceMode(serviceMode)
                .isActive(YesNoCode.YES)
                .reportCount(0)
                .isTermsAgreed(YesNoCode.YES)
                .isPrivacyAgreed(YesNoCode.YES)
                .isLocationAgreed(YesNoCode.YES)
                .isMarketingAgreed(YesNoCode.NO)
                .build();
    }


    @PrePersist
    public void prePersist() {
        // 회원 최초 생성시 사용자일경우 비활성, 관리자/매니저 일경우 활성
        if(this.role != UserRoleCode.USER) {
            this.isActive = YesNoCode.YES;
        }

        if(this.role == UserRoleCode.ADMIN) {
            this.status = UserStatusCode.NORMAL;
            this.serviceMode = ServiceModeCode.MATCH;
        }

        // 마지막 Login날짜 지금으로
        this.updateLastLogin();
    }

    /**
     * 마지막 로그인 시간 갱신 (부하 방지를 위해 1시간 단위 업데이트 권장)
     */
    public void updateLastLogin() {
        // 마지막 로그인 기록이 1시간 이내라면 업데이트 생략
        if (this.lastLoginAt != null && this.lastLoginAt.isAfter(LocalDateTime.now().minusHours(1))) {
            return;
        }
        this.lastLoginAt = LocalDateTime.now();
    }

    /**
     * 신고 카운트 증가
     */
    public void increaseReportCount() {
        this.reportCount++;
    }

    /**
     * 관리자 가입 승인 시 (친구 모드로 변경)
     */
    public void approveRegistration() {
        this.status = UserStatusCode.NORMAL;
        this.serviceMode = ServiceModeCode.FRIEND;
        this.isActive = YesNoCode.YES;
    }

    /**
     * 맞선 모드 가입 신청 시
     */
    public void applyForMatchMode() {
        if (this.serviceMode == ServiceModeCode.FRIEND) {
            this.serviceMode = ServiceModeCode.MATCH_PENDING;
        }
    }

    /**
     * 맞선 모드 최종 승인 시
     */
    public void approveMatchMode() {
        this.serviceMode = ServiceModeCode.MATCH;
    }
}
