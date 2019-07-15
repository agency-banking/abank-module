package com.agencybanking.messaging

import com.agencybanking.core.messaging.AppMessage
import com.agencybanking.core.messaging.AppMessageType
import com.agencybanking.messaging.processors.AbstractMsgProcessor
import com.agencybanking.messaging.processors.EmailProcessor
import com.agencybanking.messaging.processors.SmsProcessor
import com.agencybanking.messaging.processors.WebMsgProcessor
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

internal class MsgRecieverTest {
    @Autowired
    lateinit var msgReciever : MsgReciever
    var appMessage = AppMessage()


    @BeforeEach
    fun setUp() {
        appMessage = AppMessage()
                .also { it.appMessageType = AppMessageType.EMAIL }
//            .also { it. = AppMessageType.EMAIL }
    }

    @Test
    fun `validate message data`() {
        assertThrows<InvalidMessageException> { msgReciever.checkMessageData(appMessage) }
    }

    @Test
    fun `Get Message Processor`() {
        val msgProcessor1: AbstractMsgProcessor = msgReciever.getMessageProcessor(appMessage.appMessageType)
        assertThat(msgProcessor1, IsInstanceOf(EmailProcessor::class.java))

        appMessage.appMessageType = AppMessageType.SMS
        val msgProcessor2: AbstractMsgProcessor = msgReciever.getMessageProcessor(appMessage.appMessageType)
        assertThat(msgProcessor2, IsInstanceOf(SmsProcessor::class.java))

        appMessage.appMessageType = AppMessageType.WEB
        val msgProcessor3: AbstractMsgProcessor = msgReciever.getMessageProcessor(appMessage.appMessageType)
        assertThat(msgProcessor3, IsInstanceOf(WebMsgProcessor::class.java))
    }
}
