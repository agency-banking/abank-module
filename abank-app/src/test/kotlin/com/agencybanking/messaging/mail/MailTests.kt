package com.agencybanking.messaging.mail

import com.agencybanking.core.messaging.AppMessage
import com.agencybanking.core.messaging.AppMessageType
import com.agencybanking.messaging.MessagingModule
import com.agencybanking.messaging.processors.EmailProcessor
import com.agencybanking.messaging.email.AsyncMailJob
import com.agencybanking.messaging.email.MailQueueRepository
import com.agencybanking.messaging.queue.QueueSendState
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MailTests {
    @Autowired
    lateinit var mailQueueRepository: MailQueueRepository
    @Autowired
    lateinit var emailProcessor: EmailProcessor
    @Autowired
    lateinit var asyncMailJob: AsyncMailJob
    @Autowired
    lateinit var messagingModule: MessagingModule

    var emailMessage = AppMessage().apply {
        this.appMessageType = AppMessageType.EMAIL
        this.async = true
        this.recipients = arrayListOf("udubic@gmail.com", "dubeuzuegbu@yahoo.com.au")
        this.subject = "Test mail up"
        this.text = "Welcome to test mail texting"
    }

    @BeforeEach
    fun setUp() {
        mailQueueRepository.deleteAll()
    }

    @Test
    fun `Queue Mail`() {
        emailProcessor.processMessage(emailMessage)
        assertThat("message has to be in db to be queued", mailQueueRepository.findAll(), Matchers.hasSize(1))
    }

    @Test
    fun `Find Queued Mail`() {
        emailProcessor.processMessage(emailMessage)
        assertThat("should find queued mail", asyncMailJob.loadUnsentMsgs().hasContent(), `is`(true))
    }

    @Test
    fun `Find Failed Queued Mail`() {
        emailProcessor.processMessage(emailMessage)
        val msgQueueData = mailQueueRepository.findAll()[0].also { it.sendStatus = QueueSendState.FAILED }
        mailQueueRepository.save(msgQueueData)
        assertThat("should not find failed queued mail", asyncMailJob.loadUnsentMsgs().hasContent(), `is`(false))
    }

    @Test
    fun `Find Successful Queued Mail`() {
        emailProcessor.processMessage(emailMessage)
        val msgQueueData = mailQueueRepository.findAll()[0].also { it.sendStatus = QueueSendState.SUCCESSFUL }
        mailQueueRepository.save(msgQueueData)
        assertThat("should not find failed queued mail", asyncMailJob.loadUnsentMsgs().hasContent(), `is`(false))
    }


    @Test
    fun `Retry activated`() {
        emailProcessor.processMessage(emailMessage)
        val msgQueueData = mailQueueRepository.findAll()[0]

        val failedMsg = "Mail failed"
        asyncMailJob.failed(msgQueueData, Exception(failedMsg))
        mailQueueRepository.save(msgQueueData)
        val retryData = mailQueueRepository.findAll()[0]
        assertThat("Retry should be incremented", retryData.retries, `is`(1))
        assertThat("Retry should be incremented", retryData.sendStatus, `is`(QueueSendState.RETRY))
        assertThat("failed Message show be there", retryData.failedMsg, `is`(failedMsg))
        println("new send Date :: ${retryData.sendDate}")
    }
}