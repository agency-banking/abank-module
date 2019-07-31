/**
 * 
 */
package com.agencybanking.security.tokens;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
	Token findByTokenAndTokenType(String token, TokenType tokenType);

	List<Token> findByTokenAndDataRefAndUsedIsFalse(String token, String dataRef);
	List<Token> findByDataRefAndTokenType(String dataRef, TokenType tokenType);
}
