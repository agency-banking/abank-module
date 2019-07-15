package com.agencybanking.messaging.queue

import com.agencybanking.core.data.Data
import com.agencybanking.core.messaging.AppMessage
import com.agencybanking.core.messaging.AppMessageType
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "msg_queue")
class MsgQueueData(data: AppMessage) : Data() {
    @Id
    @SequenceGenerator(name = "msgQueGen", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "msgQueGen")
    var id: Long? = null

    @NotBlank(message = "Text cannot be blank")
    @Lob
    @Column(name = "txt", nullable = false)
    var text: String = data.text

    @Column(name = "sub")
    var subject: String = data.subject
    @Column(name = "cc")
    var copies: String = data.cc.joinToString { "," }
    @Column(name = "bcc")
    var blanks: String = data.bcc.joinToString { "," }

    @Enumerated(EnumType.STRING)
    @Column(name = "type_", nullable = false)
    var msgType: AppMessageType = data.appMessageType

    @Column(name = "async", nullable = false)
    var async: Boolean = data.async

    @Column(name = "attach_key")
    var attachmentKey: String? = data.attachmentKey
    @Column(name = "attach_url")
    var attachmentUrl: String? = data.attachmentUrl
    @Column(name = "attach_nm")
    var attachmentName: String? = data.attachmentName
    @Column(name = "rec", nullable = false, length = 500)
    var recipients: String = data.recipients.joinToString { "," }
    @Column(name = "product")
    var product: String? = data.product

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


    fun retried(){
        retries++;
    }

}