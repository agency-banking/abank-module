/**
 *
 */
package com.agencybanking.security.auth.providers.ldap;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LdapDetailsRepository extends JpaRepository<LdapDetails, Long> {

    Optional<LdapDetails> findByCompanyId(Long companyId);

    Optional<LdapDetails> findByCompanyIdAndIdNot(Long companyId, Long id);
}