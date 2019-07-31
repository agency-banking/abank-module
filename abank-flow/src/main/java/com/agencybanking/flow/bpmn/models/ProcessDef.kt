package com.agencybanking.flow.bpmn.models

import javax.xml.bind.annotation.*

/**
 * Represents a BPMN 2.0 Process
 *
 * @author dubic
 */
@XmlAccessorType(XmlAccessType.FIELD)
class ProcessDef {
    @XmlAttribute(name = "id", required = true)
    var id: String = ""

    @XmlAttribute(name = "name")
    var name: String? = null

    @XmlElement(name = "startEvent")
    var start: StartEvent? = null

    @XmlElement(name = "endEvent")
    var end: EndEvent? = null

    @XmlElement(name = "sequenceFlow")
    var flows: List<Flow> = arrayListOf()

    @XmlElement(name = "userTask")
    var userTasks: List<UserTask> = arrayListOf()


    @XmlElement(name = "serviceTask")
    var serviceTasks: List<ServiceTask> = arrayListOf()

    @XmlElement(name = "messageTask")
    var messageTasks: List<MessageTask> = arrayListOf()


    @XmlElement(name = "flowNode")
    var flowNodes: List<FlowNode> = arrayListOf()

    @XmlTransient
    var counter: Int = 0


}
