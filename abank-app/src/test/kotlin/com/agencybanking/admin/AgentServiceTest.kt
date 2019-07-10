package com.agencybanking.admin

import com.agencybanking.admin.agents.Agent
import com.agencybanking.admin.agents.AgentRepository
import com.agencybanking.admin.agents.AgentService
import com.agencybanking.core.data.UniqueFieldExistsException
import com.agencybanking.security.users.UserRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AgentServiceTest @Autowired constructor(val agentService: AgentService, val agentRepository: AgentRepository,
                                              val userRepository: UserRepository) {
    var agent = Agent()
            .also { it.email = "udubic@gmail.com" }
            .also { it.phone = "08032356801" }
            .also { it.name.firstName = "dubic" }
            .also { it.name.lastName = "uzuegbu" }
            .also { it.address.houseNo = "7" }
            .also { it.address.street = "oyewole habeeb" }
            .also { it.address.city = "gbagada" }
            .also { it.user.username = "udubic@gmail.com" }
            .also { it.user.email = "udubic@gmail.com" }
            .also { it.user.firstName = "dubic" }
            .also { it.user.lastName = "uzuegbu" }

    @BeforeEach
    fun setUp() {
        agentRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `Agent Unique Fields check Create`() {
        agentRepository.save(agent)
        val assertThrows = assertThrows<UniqueFieldExistsException> { agentService.unique(agent, null) }
        assertThat(assertThrows.messages.keys, hasSize(2))
    }

    @Test
    fun `Agent Unique Fields check update`() {
        agentRepository.save(agent)
        agentService.unique(agent, agent.id)
    }

    @Test
    fun `Agent Business Rules check`() {

    }

    @Test
    fun `Onboard Agent`() {
        agentService.onboardAgent(agent)
        val agents = agentRepository.findAll()

        assertThat("Agent was not saved in DB", agents, hasSize(1))
        assertThat("Agent was not given id", agents[0].id, `is`(notNullValue()))
        assertThat("Agent has to be active", agents[0].active, `is`(false))
        //user login saved
        val user = userRepository.findByUsername(agent.user.username)
        assertThat("User was not created", user, `is`(notNullValue()))
        assertThat("email must be unverified", user?.emailVerified, `is`(false))
    }

    @Test
    fun `Update Agent Info`() {
        agentRepository.save(agent)
        agent.name.firstName = "dubine"
        agent.email = "dubine@updated.com"
        agentService.updateAgent(agent)
        val agent = agentRepository.findById(agent.id!!).get()

        assertThat("Agent name was not updated", agent.name.firstName, `is`("dubine"))
        assertThat("Agent email was not updated", agent.email, `is`("dubine@updated.com"))
        assertThat("Agent user email was not updated", agent.user.email, `is`("dubine@updated.com"))
        //user login saved
        val user = userRepository.findByUsername(agent.user.username)
        assertThat("User email not updated", user?.email, `is`("dubine@updated.com"))
        assertThat("User firstname not updated", user?.firstName, `is`("dubine"))
    }

    @Test
    fun `Delete Agent`() {
        agentRepository.save(agent)
        agentService.delete(agent.id)
        val agents = agentRepository.findAll()
        assertThat("Agent was not deleted in DB", agents, hasSize(0))

        assertThat("User was not deleted in DB", userRepository.findAll(), hasSize(0))
    }
}