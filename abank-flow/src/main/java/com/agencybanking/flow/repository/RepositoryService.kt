package com.agencybanking.flow.repository

import com.agencybanking.core.services.BaseService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RepositoryService @Autowired constructor(val processRepository: ProcessRepository) :BaseService(){
    val log = LoggerFactory.getLogger(this.javaClass)

     fun getProcessDeployment(processDefId: String): Deployment? {
        val processDefinitions = processRepository.findByProcessIdAndActiveIsTrue(processDefId)
        log.debug("{} number or processes found for {}", processDefinitions.size, processDefId)
        return processDefinitions.firstOrNull()
    }
}