package com.jwtSecurity.repository;

import com.jwtSecurity.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUserName(String username);
    Boolean existByUserName(String username);
}
