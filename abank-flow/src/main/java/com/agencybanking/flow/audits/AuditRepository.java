/**
 * 
 */
package com.agencybanking.flow.audits;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Long> {

}