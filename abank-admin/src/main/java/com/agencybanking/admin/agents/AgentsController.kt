package com.agencybanking.admin.agents

import com.agencybanking.admin.AdminModule
import com.agencybanking.core.services.BaseController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * @author dubic
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/agents")
class AgentsController @Autowired constructor(val agentService: AgentService) : BaseController() {

    @PutMapping("/onboard")
    fun onboardAgent(@RequestBody agent: Agent) {
        val onboardAgent = agentService.onboardAgent(agent)
        ResponseEntity.ok(respondWith(onboardAgent.code).success(AdminModule.SUC_ONBOARD_AGENT, onboardAgent.name.fullName()))
    }
}