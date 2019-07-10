package com.agencybanking.admin.agents

import org.springframework.data.jpa.repository.JpaRepository

interface AgentRepository : JpaRepository<Agent, Long> {
    fun findByEmail(email: String): Agent?
    fun findByEmailAndIdNot(email: String, id: Long): Agent?
    fun findByPhone(phone: String): Agent?
    fun findByPhoneAndIdNot(phone: String, id: Long): Agent?
}