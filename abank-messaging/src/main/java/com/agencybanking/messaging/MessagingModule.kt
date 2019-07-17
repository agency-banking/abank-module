package com.agencybanking.messaging

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "messaging")
open class MessagingModule {
    val mail = Mail(
            sender = "",
            senderName = "Agency Banking",
            maximumRetries = 5,
            retryFactor = 60_000L,
            retry = true,
            fetchSize = 20,
            expiryPeriodMillis = 86_400_000L
    )

    companion object {
        val CODE = "messaging"
    }

    class Mail(val sender: String, val senderName: String, val maximumRetries: Int, val retryFactor: Long,
               val retry: Boolean, val fetchSize: Int,val expiryPeriodMillis: Long)

}
