package com.agencybanking.admin.agents

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface AgentRepository : JpaRepository<Agent, Long>, QuerydslPredicateExecutor<Agent> {

}