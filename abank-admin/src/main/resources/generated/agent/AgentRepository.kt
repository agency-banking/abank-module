/**
 * 
 */
package com.agencybanking.admin;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface AgentRepository : JpaRepository<Agent, Long> {

    fun findByPhone(phone: String): Agent?
    fun findByPhoneAndIdNot(phone: String, id: Long): Agent?
    fun findByEmail(email: String): Agent?
    fun findByEmailAndIdNot(email: String, id: Long): Agent?

}