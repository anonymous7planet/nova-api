package com.nova.anonymousplanet.system.repository;

import com.nova.anonymousplanet.core.entity.CommonCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.core
 * fileName : CommonCodeRepository
 * author : Jinhong Min
 * date : 2026-01-09
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2026-01-09      Jinhong Min      최초 생성
 * ==============================================
 */
@Repository
public interface CommonCodeRepository extends JpaRepository<CommonCodeEntity, String> {
    Optional<CommonCodeEntity> findByCodeId(String codeId);
    List<CommonCodeEntity> findRootCodesByGroupCode(String groupCode);
}
