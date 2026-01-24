package com.nova.anonymousplanet.auth.repository;

import com.nova.anonymousplanet.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
  projectName : nova-api
  packageName : com.nova.anonymousplanet.auth.repository
  fileName : UserRepository
  author : Jinhong Min
  date : 2025-10-28
  description : 
  ==============================================
  DATE            AUTHOR          NOTE
  ----------------------------------------------
  2025-10-28      Jinhong Min      최초 생성
  ==============================================
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailHash(String emailHash);

    boolean existsByEmailHash(String emailHash);

    boolean existsByCiHash(String ciHash);
}
