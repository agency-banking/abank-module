package com.agencybanking.flow.repository

import org.springframework.data.jpa.repository.JpaRepository

interface ProcessRepository : JpaRepository<Deployment, Long> {
    fun findByProcessIdAndActiveIsTrue(processDefId: String): List<Deployment>
}