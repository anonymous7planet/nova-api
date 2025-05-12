package com.nova.anonymousplanet.auth.infrastructure.persistence.repository;

import com.nova.anonymousplanet.auth.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * projectName : nova-api
 * packageName : com.nova.anonymousplanet.auth.infrastructure.persistence.repository
 * fileName : UserRepository
 * author : Jinhong Min
 * date : 2025-05-01
 * description :
 * ==============================================
 * DATE            AUTHOR          NOTE
 * ----------------------------------------------
 * 2025-05-01      Jinhong Min      최초 생성
 * ==============================================
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
