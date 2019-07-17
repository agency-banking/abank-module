package com.agencybanking.messaging.email

import com.agencybanking.core.messaging.AppMessage
import com.agencybanking.core.messaging.AppMessageType
import com.agencybanking.messaging.queue.QueueData
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "msg_queue")
class MailQueueData(data: AppMessage) : QueueData() {

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



}