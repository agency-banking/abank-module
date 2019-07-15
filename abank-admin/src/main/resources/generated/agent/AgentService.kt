package com.agencybanking.admin;

import com.agencybanking.core.services.Crud
import com.agencybanking.core.data.UniqueFieldExistsException
import org.springframework.stereotype.Service
import com.agencybanking.core.data.Money
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import com.agencybanking.core.services.BaseService
import org.springframework.util.Assert

@Service
open class AgentService @Autowired constructor(val agentRepository: AgentRepository) : BaseService() {
    private val log = LoggerFactory.getLogger(this.javaClass)

    companion object {
        val ACTION_CREATE = "CREATE"
        val ACTION_UPDATE = "UPDATE"
        val ACTION_DELETE = "DELETE"
    }

    /**
     *
     * prepare Agent - add all fields not supplied from front end.<br/>
     *validate - check all fields are correct.
     *business rules - check limits and status(Life cycle) checks
     *unique - check all unique fields don't exists in db
     *save - save data and relationships
     */
    fun createAgent(agent: Agent): Agent {
        agent.also { prepareAgent(it) }
                .also { it.validate() }
                .also { businessRules(it, ACTION_CREATE) }
                .also { unique(it, null) }

        return agentRepository.save(agent)
                .also { log.info("Agent created") }
                .also { broadcast(agent, ACTION_CREATE) }
    }


    fun prepareAgent(it: Agent) {

    }

    fun businessRules(it: Agent, action: String) {

    }

    fun unique(it: Agent, id: Long?) {
        val uniqueFieldExistsException = UniqueFieldExistsException()
        val agent0 = if (id == null) agentRepository.findByPhone(it.phone) else agentRepository.findByPhoneAndIdNot(it.phone, id)
        if (agent0 != null) {
            uniqueFieldExistsException.messages[AdminModule.ERR_PHONE_EXISTS] = listOf(it.phone)
        }
        val agent1 = if (id == null) agentRepository.findByEmail(it.email) else agentRepository.findByEmailAndIdNot(it.email, id)
        if (agent1 != null) {
            uniqueFieldExistsException.messages[AdminModule.ERR_EMAIL_EXISTS] = listOf(it.email)
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
                .also { log.info("Agent modified") }
                .also { broadcast(found, ACTION_UPDATE) }
    }

    fun delete(id: Long) {
        val agent = findById(id)
        agentRepository.deleteById(id)
                .also { log.info("Agent deleted") }
                .also { broadcast(agent, ACTION_DELETE) }
    }

    fun list(agent: Agent): Any? {
        return agentRepository.findAll()
    }
}
