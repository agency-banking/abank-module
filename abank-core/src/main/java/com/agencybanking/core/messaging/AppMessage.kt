package com.agencybanking.core.messaging

import org.eclipse.persistence.oxm.annotations.XmlCDATA
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement

class AppMessage {

    @XmlElement(name = "messageType")
    var appMessageType: AppMessageType = AppMessageType.EMAIL

    @XmlCDATA
    @XmlElement(name = "text")
    var text: String = ""

    @XmlElement(name = "recipients")
    var recipients = listOf<String>()

    @XmlElement(name = "copies")
    var cc = listOf<String>()

    @XmlElement(name = "blind-copies")
    var bcc = listOf<String>()

    @XmlCDATA
    @XmlElement(name = "subject")
    var subject: String = ""

    @XmlElement(name = "attachment-url")
    var attachmentUrl: String? = null

    @XmlElement(name = "attachment-name")
    var attachmentName: String? = null

    @XmlElement(name = "attachment-key")
    var attachmentKey: String? = null

    @XmlAttribute
    var async: Boolean = false
    @XmlAttribute
    var product: String? = null
    @XmlAttribute
    var module: String? = null

    var expiryPeriodMillis: Long? = null
//    @XmlAttribute
//    var ref: String? = null
//
//    @XmlAttribute
//    var resend: Int = 0
//
//    @XmlTransient
//    var status: String? = null
//    @XmlTransient
//    var templateName: String? = null
}