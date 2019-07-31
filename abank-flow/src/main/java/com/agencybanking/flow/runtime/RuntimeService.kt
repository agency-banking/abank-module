package com.agencybanking.flow.runtime

import com.agencybanking.flow.NodeUtils
import com.agencybanking.flow.bpmn.models.BPMNNode
import com.agencybanking.flow.bpmn.models.EndEvent
import com.agencybanking.flow.bpmn.models.ProcessDefinition
import com.agencybanking.flow.bpmn.models.UserTask
import com.agencybanking.flow.repository.Deployment
import com.agencybanking.flow.repository.RepositoryService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RuntimeService @Autowired constructor(val repositoryService: RepositoryService,
                                            val processInstanceService: ProcessInstanceService){
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun startProcessInstance(processId: String, variables: Map<String, Any>): ProcessInstance? {
        //get definition
        val deployment = repositoryService.getProcessDeployment(processId)

        deployment?.let {
            log.debug("Found process : {} " + deployment.processId)
            log.debug(deployment.definitions)
            println("-----------------------------------")
            val process = ProcessInstance().apply {
                this.activeNode = "start"
                this.processId = processId
                this.hasApproval = checkApprovalTask(deployment, variables)

            }

            this.processInstanceService.runDeployment(deployment, variables)
            //return instance details

            return process
        }
        log.warn("No process definition found for id {}", processId)
        return null
    }

    fun checkApprovalTask(deployment: Deployment, variables: Map<String, Any>): Boolean {
        var approvalTask = false
        val definitions = ProcessDefinition.from(deployment.definitions)
        if (deployment.hasApproval) {
            //there is user task, so lets check the flows
            val startNode = definitions.getStartNode()
            approvalTask = hasApprovalTask(startNode, definitions, variables, false)
        }
        return approvalTask
    }

    fun hasApprovalTask(currentNode: BPMNNode, def: ProcessDefinition, variables: Map<String, Any>, nextNode: Boolean): Boolean {
        var currentNode = currentNode
        if (nextNode) {
            currentNode = NodeUtils.nextNode(currentNode.id, def, variables)
        }
        if (currentNode is EndEvent || currentNode == null) {
            return false
        }
        if (currentNode is UserTask) {
            return true
        }
        val nextNd = NodeUtils.nextNode(currentNode.id, def, variables)
        return hasApprovalTask(nextNd, def, variables, false)
    }
}