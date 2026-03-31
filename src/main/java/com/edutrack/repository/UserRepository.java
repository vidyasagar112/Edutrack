package com.edutrack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutrack.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // find user by email (used in login)
    Optional<User> findByEmail(String email);

    // check if email already exists (used in register)
    boolean existsByEmail(String email);
}