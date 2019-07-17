package com.agencybanking.messaging.processors

import com.agencybanking.core.messaging.AppMessage
import com.agencybanking.messaging.MessagingModule
import com.agencybanking.messaging.email.MailEngine
import com.agencybanking.messaging.email.MailQueueData
import com.agencybanking.messaging.queue.QueueSendState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmailProcessor @Autowired constructor(val mailEngine: MailEngine, val module: MessagingModule) : AbstractMsgProcessor() {
    override fun processMessage(appMessage: AppMessage) {
        val mailQueue = MailQueueData(appMessage)
        validateForEmail(mailQueue)
        mailQueue.retry = module.mail.retry
        mailQueue.expiryDate = Date(System.currentTimeMillis() + (appMessage.expiryPeriodMillis?: module.mail.expiryPeriodMillis))
        if (mailQueue.async) {
            mailQueueRepository.save(mailQueue)
        } else {
            this.mailEngine.send(mailQueue)
            mailQueue.sendStatus = QueueSendState.SUCCESSFUL
            mailQueueRepository.save(mailQueue)
        }
    }

    private fun validateForEmail(mailQueue: MailQueueData) {
        mailQueue.validate()
        if (mailQueue.subject.isBlank())
            throw IllegalArgumentException("Subject is required for email")
    }
}