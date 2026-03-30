package com.edutrack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutrack.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // find role by name
    // example: findByName("ROLE_STUDENT")
    Optional<Role> findByName(String name);
}