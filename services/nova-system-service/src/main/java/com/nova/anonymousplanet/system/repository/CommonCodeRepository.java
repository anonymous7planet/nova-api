package com.nova.anonymousplanet.system.repository;

import com.nova.anonymousplanet.system.entity.CommonCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    @Query("SELECT DISTINCT c FROM CommonCodeEntity c " +
            "LEFT JOIN FETCH c.children " +
            "WHERE c.groupCode = :groupCode AND c.parent IS NULL")
    List<CommonCodeEntity> findRootCodesByGroupCode(@Param("groupCode") String groupCode);
}
