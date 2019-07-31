package com.agencybanking.admin.agents

import com.agencybanking.admin.AdminModule
import com.agencybanking.core.data.UniqueFieldExistsException
import com.agencybanking.core.services.BaseService
import com.agencybanking.core.utils.Utils
import com.agencybanking.security.users.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.time.LocalDateTime
import java.util.*

@Service
open class AgentService @Autowired constructor(val agentRepository: AgentRepository, val userRepository: UserRepository) : BaseService() {
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
                .also { it.hasEmail().hasPhone() }
                .also { businessRules(it, ACTION_CREATE) }
                .also { unique(it, null) }
                .also { it.agentId = buildAgentId(it) }

//        agent.fillInUser()
        return agentRepository.save(agent)
                .also { log.info("Agent onboarded ${agent.user.name.fullName()}") }
                .also { broadcast(agent, ACTION_CREATE) }
    }

    fun buildAgentId(it: Agent): String {
        val c1 = it.user.name.firstName.toCharArray()[0]
        val c2 = it.user.name.lastName.toCharArray()[0]
        val now = LocalDateTime.now()
        val year = now.year
        val dayOfYear = Utils.leftPad(now.dayOfYear.toString(), "0", 3)
//        val hour = Utils.leftPad(now.hour.toString(), "0", 2)
        val sec = Utils.leftPad(now.second.toString(), "0", 2)
        val rand = Utils.leftPad(Random().nextInt(100).toString(), "0", 3)
        return "AG$c1$c2$year$dayOfYear$sec$rand".toUpperCase()
    }


    fun prepareAgent(it: Agent) {

    }

    fun businessRules(it: Agent, action: String) {
    }

    fun unique(it: Agent, id: Long?) {
        val uniqueFieldExistsException = UniqueFieldExistsException()
        if (it.user.email.isNotBlank()) {
            val agent = if (id == null) userRepository.findByEmail(it.user.email) else userRepository.findByEmailAndIdNot(it.user.email, id)
            if (agent != null) {
                uniqueFieldExistsException.messages[AdminModule.ERR_EMAIL_EXISTS] = listOf(it.user.email)
            }
        }
        val agphone = if (id == null) userRepository.findByPhoneNumber(it.user.phoneNumber) else userRepository.findByPhoneNumberAndIdNot(it.user.phoneNumber, id)
        if (agphone != null) {
            uniqueFieldExistsException.messages[AdminModule.ERR_PHONE_EXISTS] = listOf(it.user.phoneNumber)
        }
        if (uniqueFieldExistsException.messages.isNotEmpty()) {
            uniqueFieldExistsException.messages.forEach { println(it) }
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
                .also { unique(it, found.user.id) }
//        agent.fillInUser()
        return agentRepository.save(found)
                .also { log.info("Agent modified ${agent.user.name.fullName()}") }
                .also { broadcast(found, ACTION_UPDATE) }
    }

    fun delete(id: Long) {
        val agent = findById(id)
        agentRepository.deleteById(id)
                .also { log.info("Agent deleted ${agent.user.name.fullName()}") }
                .also { broadcast(agent, ACTION_DELETE) }
    }

    fun list(agent: Agent): Any? {
        return agentRepository.findAll()
    }
}