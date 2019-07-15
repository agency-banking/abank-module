package com.agencybanking.messaging.processors

import com.agencybanking.core.messaging.AppMessage
import org.springframework.stereotype.Service

@Service
class SmsProcessor: AbstractMsgProcessor() {
    override fun processMessage(appMessage: AppMessage) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}