package com.agencybanking.flow

import com.agencybanking.core.services.BizEvent
import com.agencybanking.flow.audits.AuditRepository
import com.agencybanking.flow.data.CaseProduct
import com.agencybanking.flow.repository.Deployment
import com.agencybanking.flow.repository.ProcessRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationEventPublisher

@SpringBootTest
class FlowTriggerTests {
    @Autowired
    lateinit var flowTrigger: FlowTrigger
    @Autowired
    lateinit var processRepository: ProcessRepository
    @Autowired
    lateinit var auditRepository: AuditRepository
    @Autowired
    lateinit var publisher: ApplicationEventPublisher

    @BeforeEach
    fun setUp() {
        processRepository.deleteAll()
        auditRepository.deleteAll()
    }

    fun buildProcess() {
        val deployment = Deployment().apply {
            this.active = true
            this.definitions= ""
        }
    }

    @Test
    fun `Audit log test`() {
        //basis
        val caseProduct = CaseProduct().apply {
            name = "Chinedu Nwakama"
            registered = true
        }
        val action = "CREATE"
        this.publisher.publishEvent(
                BizEvent.of(caseProduct).ref(caseProduct.id).module(caseProduct.module()).product(caseProduct.product()).action(action)
                        .fireApproval(true))
        //
        assertThat("Should haved audited", auditRepository.findAll().size, Matchers.greaterThan(0))
    }

    @Test
    fun `Get Process Deployment`() {
        assertThat("Should find a process deployment", processRepository.findByProcessIdAndActiveIsTrue("caseproduct_create").size, `is`(1))
    }

    @Test
    fun `Has Approval task`() {

    }
}