package com.agencybanking.admin.agents

import com.agencybanking.admin.AdminModule
import com.agencybanking.core.data.UniqueFieldExistsException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.Assert

@Service
class AgentService @Autowired constructor(val agentRepository: AgentRepository) {
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
                .also { businessRules(it, "CREATE") }
                .also { unique(it, null) }

        agent.fillInUser()
        return agentRepository.save(agent)
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
                .also { businessRules(it, "UPDATE") }
                .also { unique(it, found.id) }
        agent.fillInUser()
        return agentRepository.save(found)
    }

    fun delete(id: Long?) {
        agentRepository.deleteById(id)
    }


}