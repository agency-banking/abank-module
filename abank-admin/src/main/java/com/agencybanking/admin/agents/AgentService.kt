package com.agencybanking.admin.agents

import com.agencybanking.admin.AdminModule
import com.agencybanking.core.data.UniqueFieldExistsException
import com.agencybanking.core.services.BaseService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.Assert

@Service
open class AgentService @Autowired constructor(val agentRepository: AgentRepository) : BaseService() {
    private val log = LoggerFactory.getLogger(this.javaClass)

    companion object {
        val ACTION_CREATE = "ONBOARD"
        val ACTION_UPDATE = "UPDATE"
        val ACTION_DELETE = "DELETE"
    }

    /**
     *
     * prepare agent - add all fields not supplied from front end.<br/>
     *validate - check all fields are correct.
     *business rules - check limits and status(Life cycle) checks
     *unique - check all unique fields don't exists in db
     *save - save data and relationships
     */
    fun onboardAgent(agent: Agent): Agent {
        agent.also { prepareAgent(it) }
                .also { it.validate() }
                .also { businessRules(it, ACTION_CREATE) }
                .also { unique(it, null) }

        agent.fillInUser()
        return agentRepository.save(agent)
                .also { log.info("Agent onboarded ${agent.name.fullName()}") }
                .also { broadcast(agent, ACTION_CREATE) }
    }


    fun prepareAgent(it: Agent) {

    }

    fun businessRules(it: Agent, action: String) {

    }

    fun unique(it: Agent, id: Long?) {
        val uniqueFieldExistsException = UniqueFieldExistsException()
        if (it.email.isNotBlank()) {
            val agent = if (id == null) agentRepository.findByEmail(it.email) else agentRepository.findByEmailAndIdNot(it.email, id)
            if (agent != null) {
                uniqueFieldExistsException.messages[AdminModule.ERR_EMAIL_EXISTS] = listOf(it.email)
            }
        }
        val agphone = if (id == null) agentRepository.findByPhone(it.phone) else agentRepository.findByPhoneAndIdNot(it.phone, id)
        if (agphone != null) {
            uniqueFieldExistsException.messages[AdminModule.ERR_PHONE_EXISTS] = listOf(it.phone)
        }
        if (uniqueFieldExistsException.messages.isNotEmpty()) {
            throw uniqueFieldExistsException
        }
    }

    fun findById(id: Long) = agentRepository.findById(id).orElseThrow { IllegalStateException("Resource not found") }

    fun updateAgent(agent: Agent): Agent {
        Assert.notNull(agent.id, "Agent must have id")
        val found = findById(agent.id!!)
        found.copyForUpdate(agent)
        found.also { prepareAgent(it) }
                .also { it.validate() }
                .also { businessRules(it, ACTION_UPDATE) }
                .also { unique(it, found.id) }
        agent.fillInUser()
        return agentRepository.save(found)
                .also { log.info("Agent modified ${agent.name.fullName()}") }
                .also { broadcast(found, ACTION_UPDATE) }
    }

    fun delete(id: Long) {
        val agent = findById(id)
        agentRepository.deleteById(id)
                .also { log.info("Agent deleted ${agent.name.fullName()}") }
                .also { broadcast(agent, ACTION_DELETE) }
    }

    fun list(agent: Agent): Any? {
        return agentRepository.findAll()
    }
}