package com.agencybanking.security.auth;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepo extends JpaRepository<LoginHistory, Long> {
    Page<LoginHistory> findByLoginId(String loginId, Pageable p);
}
