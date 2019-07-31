/**
 * 
 */
package com.agencybanking.security.auth.providers.tokenserver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TokenServerRepository extends JpaRepository<TokenServer, Long> {

}