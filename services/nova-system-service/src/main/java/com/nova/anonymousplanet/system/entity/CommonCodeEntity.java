package com.nova.anonymousplanet.system.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core.entity
 * fileName : CommonCodeEntity
 * author : Jinhong Min
 * date : 2026-01-09
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-09      Jinhong Min      최초 생성
 * ==============================================
 */

@Entity
@Table(name = "tb_common_code")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonCodeEntity {

    @Id
    @Comment("코드 식별자 (예: H_SOCCER, REL_CHRISTIAN)")
    @Column(name = "code_id", length = 20)
    private String codeId;

    @Comment("그룹 코드 (예: HOBBY, RELIGION, EDUCATION)")
    @Column(name = "group_code", nullable = false, length = 20)
    private String groupCode;

    @Comment("코드 명칭 (예: 축구, 기독교) - 한국어 명칭")
    @Column(name = "code_name", nullable = false, length = 100)
    private String codeName;

    @Comment("영어 명칭")
    @Column(name = "code_name_en", nullable = false, length = 100)
    private String codeNameEn;

    @Comment("일본어 명칭")
    @Column(name = "code_name_ja", length = 100)
    private String codeNameJa;

    @Comment("중국어 명칭")
    @Column(name = "code_name_zh", length = 100)
    private String codeNameZh;

    @Comment("정렬 순서")
    @Column(name = "sort_order")
    private Integer sortOrder;

    @Comment("사용 여부 (Y/N)")
    @Column(name = "is_used", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private String isUsed;

    @Comment("프론트 노출 여부 (Y/N)")
    @Column(name = "is_display", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private String isDisplay;

    @Comment("부모 코드 ID (계층형 구조)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_code_id")
    private CommonCodeEntity parent;

    @Comment("자식 코드 리스트")
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @OrderBy("sortOrder ASC")
    private List<CommonCodeEntity> children = new ArrayList<>();

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "modified_by")
    private Long modifiedBy;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    /**
     * [정적 팩토리 메서드] 공통 코드 생성을 위한 함수
     */
    public static CommonCodeEntity create(String codeId, String groupCode, String name, String nameEn,
                                          String nameJa, String nameZh, CommonCodeEntity parent, Integer sortOrder) {
        CommonCodeEntity entity = new CommonCodeEntity();
        entity.codeId = codeId;
        entity.groupCode = groupCode;
        entity.codeName = name;
        entity.codeNameEn = nameEn;
        entity.codeNameJa = nameJa;
        entity.codeNameZh = nameZh;
        entity.parent = parent;
        entity.sortOrder = (sortOrder != null) ? sortOrder : 0;
        entity.isUsed = "Y";
        entity.isDisplay = "Y";
        return entity;
    }

    /**
     * [비즈니스 로직] 다국어 명칭 선택 반환
     */
    public String getLocaleName(String lang) {
        if (lang == null) return this.codeName;
        return switch (lang.toLowerCase()) {
            case "en" -> this.codeNameEn;
            case "ja" -> (this.codeNameJa != null) ? this.codeNameJa : this.codeName;
            case "zh" -> (this.codeNameZh != null) ? this.codeNameZh : this.codeName;
            default -> this.codeName;
        };
    }

    /**
     * [비즈니스 로직] 코드 정보 수정
     */
    public void updateInfo(String name, String nameEn, String nameJa, String nameZh,
                           Integer sortOrder, String isUsed, String isDisplay) {
        this.codeName = name;
        this.codeNameEn = nameEn;
        this.codeNameJa = nameJa;
        this.codeNameZh = nameZh;
        this.sortOrder = sortOrder;
        this.isUsed = isUsed;
        this.isDisplay = isDisplay;
    }

    /**
     * [비즈니스 로직] 사용 여부 토글
     */
    public void changeUseStatus(boolean use) {
        this.isUsed = use ? "Y" : "N";
    }

    /**
     * [비즈니스 로직] 노출 여부 토글
     */
    public void changeDisplayStatus(boolean display) {
        this.isDisplay = display ? "Y" : "N";
    }

    /**
     * [비즈니스 로직] 자식 코드 추가
     */
    public void addChild(CommonCodeEntity child) {
        this.children.add(child);
        if (child.getParent() != this) {
            child.setParent(this);
        }
    }

    private void setParent(CommonCodeEntity parent) {
        this.parent = parent;
    }
}
