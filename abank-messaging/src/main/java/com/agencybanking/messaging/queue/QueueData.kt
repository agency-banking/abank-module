package com.agencybanking.messaging.queue

import com.agencybanking.core.data.Data
import com.agencybanking.core.utils.Utils
import com.agencybanking.messaging.MessagingModule
import java.util.*
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.Version

@MappedSuperclass
abstract class QueueData : Data() {

    @Column(name = "send_date", nullable = false)
    var sendDate = Date()
    @Column(name = "create_date", nullable = false)
    var createDate = Date()
    @Column(name = "send_status", nullable = false)
    var sendStatus = QueueSendState.WAITING
    @Column(name = "retries", nullable = false)
    var retries = 0
    @Column(name = "failed_msg", length = 500)
    var failedMsg = ""
    @Column(name = "retry")
    var retry = false
    @Column(name = "exp_date")
    var expiryDate: Date? = null
    @Version
    var version: Long = 0L

    fun successful() {
        sendStatus = QueueSendState.SUCCESSFUL
    }

    fun failed(e: Exception, module: MessagingModule) {
        sendStatus = QueueSendState.FAILED
        failedMsg = Utils.first(e.message, 500)
    }

    fun doRetry(maxRetries: Int, retryFactor: Long) {
        if (retry && retries <= maxRetries) {
            retries++
            sendStatus = QueueSendState.RETRY
            sendDate = nextSendDate(retries, retryFactor)
        }

    }

    /**
     * f(x) = (retryfactor)x
     *
     * @param retries
     * @return
     */
    private fun nextSendDate(retries: Int, retryFactor: Long): Date {
        val millisToAdd = retries * retryFactor
        return Date(System.currentTimeMillis() + millisToAdd)
    }
}