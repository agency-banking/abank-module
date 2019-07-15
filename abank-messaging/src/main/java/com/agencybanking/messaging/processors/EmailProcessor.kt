package com.agencybanking.messaging.processors

import com.agencybanking.core.messaging.AppMessage
import com.agencybanking.messaging.email.MailEngine
import com.agencybanking.messaging.queue.MsgQueueData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmailProcessor @Autowired constructor(val mailEngine: MailEngine) : AbstractMsgProcessor() {
    override fun processMessage(appMessage: AppMessage) {
        val msgQueue = MsgQueueData(appMessage)
        msgQueue.validate()

        if (msgQueue.async) {
            msgQueueRepository.save(msgQueue)
        } else
            msgQueueRepository.save(msgQueue)
            this.mailEngine.send(msgQueue)
    }
}