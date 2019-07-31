package com.agencybanking.flow.audits;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface IOAuditRepository extends JpaRepository<IOAudit, Long>, QuerydslPredicateExecutor<IOAudit> {
}
