package com.agencybanking.flow

import com.agencybanking.core.services.BizEvent
import com.agencybanking.core.utils.Utils
import com.agencybanking.flow.data.CaseProduct
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.`when`


internal class FlowTriggerTest {
    @Mock
    lateinit var flowTrigger : FlowTrigger

    val caseProduct = CaseProduct()

    val bizEvent = BizEvent().apply {
        product = caseProduct.product()
        action = "CREATE"
    }

//    @BeforeEach
//    fun setMockOutput() {
//        `when`(flowTrigger.c).thenReturn("Hello Mockito From Repository")
//    }

    @Test
    fun concoctProcessId() {
        assertThat("Process id should be {product_action}",AppflowUtil.concoctProcessId(bizEvent),`is`("${caseProduct.product()}_create"))
    }
}