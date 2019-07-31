package com.agencybanking.flow.bpmn.models

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement

//@XmlAccessorType(XmlAccessType.FIELD)
class Flow {
    @XmlAttribute(name = "id", required = true)
    var id: String? = null
    @XmlAttribute(name = "sourceRef", required = true)
    var sourceRef: String = ""
    @XmlAttribute(name = "targetRef", required = true)
    var targetRef: String = ""
    @XmlElement(name = "conditionExpression")
    var condition: ConditionExpression? = null
}
