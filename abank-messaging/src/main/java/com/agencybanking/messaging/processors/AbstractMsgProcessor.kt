package com.agencybanking.messaging.processors

import com.agencybanking.core.messaging.AppMessage
import com.agencybanking.messaging.queue.MsgQueueRepository

abstract class AbstractMsgProcessor {
    lateinit var msgQueueRepository: MsgQueueRepository

    abstract fun processMessage(appMessage: AppMessage)
}