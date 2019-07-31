package com.agencybanking.flow

import com.agencybanking.core.el.ExpressionBuilder
import com.agencybanking.core.security.IdentityService
import com.agencybanking.core.services.BizEvent
import com.agencybanking.flow.AppflowUtil.concoctProcessId
import com.agencybanking.flow.audits.AuditService
import com.agencybanking.flow.runtime.RuntimeService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class FlowTrigger @Autowired constructor(val auditService: AuditService,
                                         val identityService: IdentityService,
                                         val expressionBuilder: ExpressionBuilder,
                                         val runtimeService: RuntimeService) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @EventListener
    fun start(bizEvent: BizEvent) {
        val authenticatedUser = identityService.authenticatedUser
        bizEvent
                .also { startProcess(it, authenticatedUser) }
                .also { auditService.log(authenticatedUser, it) }

    }

    fun startProcess(event: BizEvent, authenticatedUser: String) {
        if (event.isFireAppflow) {
            //start process by key
            val processId = concoctProcessId(event)

            val variables = this.expressionBuilder.mapObjects(event.values)
            val processInstance = runtimeService.startProcessInstance(processId, variables)
        }
    }


}