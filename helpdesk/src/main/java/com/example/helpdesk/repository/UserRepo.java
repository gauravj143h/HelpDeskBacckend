package com.example.helpdesk.repository;

import com.example.helpdesk.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity,Long> {
    Boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);

}
