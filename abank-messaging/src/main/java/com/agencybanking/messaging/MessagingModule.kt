package com.agencybanking.messaging

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "messaging")
open class MessagingModule {
    val mail = Mail(
            sender = "",
            senderName = "Agency Banking"
    )

    companion object {
        val CODE = "messaging"
    }

    class Mail(val sender: String,val senderName: String)

}
