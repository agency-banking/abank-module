/**
 * 
 */
package com.agencybanking.security.jwt.session;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author dubic
 *
 */
public interface SessionRepository extends JpaRepository<Session, String>, QuerydslPredicateExecutor<Session> {

	List<Session> findByUsername(String username);
	
	@Query("select s from Session s where CURRENT_TIMESTAMP > s.expires order by s.expires")
	List<Session> getExpiredSessions(Pageable page);

//	Session findByUsernameAndToken(String username, String token);

	@Modifying
	@Transactional
	@Query("delete from Session s where s.id = ?1")
	int remove(String id);

	List<Session> findByUsername(String username, Sort sort);

}
