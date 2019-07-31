package com.agencybanking.security.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPersRepository extends JpaRepository<UserPermission, Long> {
    List<UserPermission> findByUsername(String username);
}
