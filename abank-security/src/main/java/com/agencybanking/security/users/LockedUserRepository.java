/**
 * 
 */
package com.agencybanking.security.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author dubic
 *
 */
public interface LockedUserRepository extends JpaRepository<LockedUser, Long> {

	Optional<LockedUser> findByUserId(Long userId);
	
}
