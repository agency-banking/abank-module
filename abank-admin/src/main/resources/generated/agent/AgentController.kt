package com.agencybanking.admin;

import com.agencybanking.admin.AdminModule
import com.agencybanking.core.services.BaseController
import com.agencybanking.core.web.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

/**
 * @author dubic
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/agents")
class AgentController @Autowired constructor(val agentService: AgentService) : BaseController() {

    @PutMapping("/create")
    fun createAgent(@RequestBody agent: Agent): ResponseEntity<Response> {
        val createAgent = agentService.createAgent(agent)
        return ok(respondWith(createAgent.code).success(AdminModule.SUC_CREATE_AGENT, createAgent.name.fullName()))
    }

    @PatchMapping("/updateAgent")
    fun updateAgentInfo(@RequestBody agent: Agent): ResponseEntity<Response> {
        val updatedAgent = agentService.updateAgent(agent)
        return ok(respondWith(updatedAgent).success(AdminModule.SUC_UPDATE_AGENT, updatedAgent.name.fullName()))
     }

     @GetMapping("/findById/{id}")
     fun findById(@PathVariable("id") agent: Agent): ResponseEntity<*> = ok(respondWith(agent))

     @PostMapping("/list")
     fun findAll(@RequestBody agent: Agent): ResponseEntity<*> = ok(respondWith(agentService.list(agent)))

     @DeleteMapping("/delete/{id}")
     fun delete(@PathVariable("id") id: Long): ResponseEntity<*> {
        agentService.delete(id)
        return ok(respond().success(adminModule.SUC_DELETE_AGENT))
     }
}
