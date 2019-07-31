package com.agencybanking.flow.bpmn.models

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.Data
import org.springframework.util.Assert
import java.util.*
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlTransient

@Data
@XmlAccessorType(XmlAccessType.FIELD)
open class BPMNNode {
    @XmlAttribute(name = "id", required = true)
    var id: String = ""
    @XmlAttribute(name = "name")
    var name: String = ""
    @XmlTransient
    var recipientNames: String? = null
    @XmlTransient
    var type: String? = null
    @JsonIgnore
    @XmlTransient
    var expressions: List<String> = ArrayList()
    @XmlTransient
    var negateExpressions: Boolean = false
    @XmlTransient
    var deleteable = true

    @XmlTransient
    open fun pauseExecution(): Boolean {
        return false
    }

    open fun validate() {
        Assert.hasLength(id, "BPMN Node id is required")
    }
}
