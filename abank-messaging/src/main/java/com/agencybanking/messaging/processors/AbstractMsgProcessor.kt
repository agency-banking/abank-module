package com.agencybanking.messaging.processors

import com.agencybanking.core.messaging.AppMessage
import com.agencybanking.messaging.email.MailQueueRepository
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractMsgProcessor {
    @Autowired
    lateinit var mailQueueRepository: MailQueueRepository

    abstract fun processMessage(appMessage: AppMessage)
}