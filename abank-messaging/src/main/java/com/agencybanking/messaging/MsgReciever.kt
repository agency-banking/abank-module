package com.agencybanking.messaging

import com.agencybanking.core.messaging.AppMessage
import com.agencybanking.core.messaging.AppMessageType
import com.agencybanking.messaging.processors.AbstractMsgProcessor
import com.agencybanking.messaging.processors.EmailProcessor
import com.agencybanking.messaging.processors.SmsProcessor
import com.agencybanking.messaging.processors.WebMsgProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class MsgReciever @Autowired constructor(val emailProcessor: EmailProcessor,
                                         val smsProcessor: SmsProcessor,
                                         val webMsgProcessor: WebMsgProcessor) {
    @EventListener
    fun receive(appMessage: AppMessage) {
        checkMessageData(appMessage)
        getMessageProcessor(appMessage.appMessageType).processMessage(appMessage)
    }

    fun checkMessageData(appMessage: AppMessage) {
        val messages = mutableListOf<String>()
        if (appMessage.recipients.isEmpty()){
            messages.add(InvalidMessageException.RECIPIENTS_REQUIRED)
        }
        if (messages.isNotEmpty()) {
            throw InvalidMessageException(messages)
        }
    }

    fun getMessageProcessor(appMessageType: AppMessageType): AbstractMsgProcessor {
        return when (appMessageType) {
            AppMessageType.EMAIL -> emailProcessor
            AppMessageType.WEB -> webMsgProcessor
            AppMessageType.SMS -> smsProcessor
        }
    }

}