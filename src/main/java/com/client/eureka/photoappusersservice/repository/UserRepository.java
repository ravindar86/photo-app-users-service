package com.client.eureka.photoappusersservice.repository;

import com.client.eureka.photoappusersservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String username);
}
