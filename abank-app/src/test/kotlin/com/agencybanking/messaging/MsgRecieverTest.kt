package com.agencybanking.messaging

import com.agencybanking.core.messaging.AppMessage
import com.agencybanking.core.messaging.AppMessageType
import com.agencybanking.messaging.processors.AbstractMsgProcessor
import com.agencybanking.messaging.processors.EmailProcessor
import com.agencybanking.messaging.processors.SmsProcessor
import com.agencybanking.messaging.processors.WebMsgProcessor
import com.agencybanking.messaging.email.MailQueueRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class MsgRecieverTest {
    @Autowired
    lateinit var msgReciever : MsgReciever
    @Autowired
    lateinit var mailQueueRepository: MailQueueRepository

    var emailMessage = AppMessage().apply {
        this.appMessageType = AppMessageType.EMAIL
        this.async = true
        this.recipients = arrayListOf("udubic@gmail.com","dubeuzuegbu@yahoo.com.au")

    }


    @BeforeEach
    fun setUp() {

    }

    @Test
    fun `validate message data`() {
//        emailMessage.text = ""
        assertThrows<InvalidMessageException> { msgReciever.checkMessageData(emailMessage) }
    }

    @Test
    fun `Get Message Processor`() {
        val msgProcessor1: AbstractMsgProcessor = msgReciever.getMessageProcessor(emailMessage.appMessageType)
        assertThat(msgProcessor1, IsInstanceOf(EmailProcessor::class.java))

        emailMessage.appMessageType = AppMessageType.SMS
        val msgProcessor2: AbstractMsgProcessor = msgReciever.getMessageProcessor(emailMessage.appMessageType)
        assertThat(msgProcessor2, IsInstanceOf(SmsProcessor::class.java))

        emailMessage.appMessageType = AppMessageType.WEB
        val msgProcessor3: AbstractMsgProcessor = msgReciever.getMessageProcessor(emailMessage.appMessageType)
        assertThat(msgProcessor3, IsInstanceOf(WebMsgProcessor::class.java))
    }


}
