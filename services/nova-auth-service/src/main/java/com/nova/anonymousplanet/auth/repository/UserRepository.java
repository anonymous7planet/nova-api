package com.nova.anonymousplanet.auth.repository;

import com.nova.anonymousplanet.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

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
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUuid(String uuid);

    Optional<UserEntity> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
