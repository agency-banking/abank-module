package com.agencybanking.flow.bpmn.models

import com.agencybanking.core.data.Data
import com.agencybanking.flow.MalformedProcessException
import org.springframework.util.Assert
import org.springframework.util.Assert.notNull
import java.io.StringReader
import java.io.StringWriter
import java.util.*
import javax.validation.constraints.NotNull
import javax.xml.bind.JAXB
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * @author dubic
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "definitions")
class ProcessDefinition : Data() {
    @XmlElement(name = "process")
    var process = ProcessDef()

    fun getStartNode(): StartEvent =
            this.process.start?: throw MalformedProcessException("Start event not found in process definition. Consider to include a start node")

    fun getEndNode(): EndEvent =
            this.process.end?: throw MalformedProcessException("End event not found in process definition. Consider to include an end node")

    val allNodes: Collection<BPMNNode>
        get() {
            val nodes = arrayListOf<BPMNNode>()
            nodes.addAll(if (this.process.userTasks != null) this.process.userTasks else arrayListOf<BPMNNode>())
            nodes.addAll(if (this.process.serviceTasks != null) this.process.serviceTasks else arrayListOf<BPMNNode>())
            nodes.addAll(if (this.process.messageTasks != null) this.process.messageTasks else arrayListOf<BPMNNode>())
            nodes.addAll(if (this.process.flowNodes != null) this.process.flowNodes else arrayListOf<BPMNNode>())
            this.process.end?.let { nodes.add(it) }
            return nodes
        }

    override fun toString(): String {
        val out = StringWriter()
        JAXB.marshal(this, out)
        return out.toString()
    }

    fun getNextFlows(node: String): List<Flow> {
        return this.process.flows.filter { it.sourceRef.equals(node, ignoreCase = true) }.toList()
    }

    /**
     * gets the next BPMNNode with id specified in the target txnreference of this flow
     *
     * @param flow flow with targetRef
     * @return next node in the process
     */
    fun nextNode(flow: Flow): BPMNNode? {
        val n = allNodes.firstOrNull { it.id.equals(flow.targetRef,ignoreCase = true) }
        return n
    }

    fun validateDefinitions() {
        val nodeIds = ArrayList<String>()
        this.allNodes.forEach {
            System.out.println(it)
            it.validate()
            if (nodeIds.contains(it.id)) {
                throw IllegalStateException("Node id already in process: " + it.id)
            }
            nodeIds.add(it.id)
        }
    }

    /**
     * find a node in this process by its id
     *
     * @param id
     * @return the found <tt>BPMNNode</tt>
     */
    fun getNode(id: String): BPMNNode {
        return allNodes.first { it.id.equals(id, ignoreCase = true) }
    }

    companion object {

        fun from(xml: String): ProcessDefinition {
            val `in` = StringReader(xml)
            return JAXB.unmarshal<ProcessDefinition>(`in`, ProcessDefinition::class.java)
        }

        fun fromDef(@NotNull(message = "Null ProcessDef supplied") processDef: ProcessDef): ProcessDefinition {
            Assert.notNull(processDef, "Process Def must be supplied from front end")
            val definition = ProcessDefinition()
            definition.process = processDef
            return definition
        }
    }
}
