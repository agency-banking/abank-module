package com.agencybanking.admin

import com.agencybanking.admin.agents.Agent
import com.agencybanking.admin.agents.AgentRepository
import com.agencybanking.admin.agents.AgentService
import com.agencybanking.core.data.UniqueFieldExistsException
import com.agencybanking.core.utils.Utils
import com.agencybanking.security.users.UserRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc(secure = false)
@SpringBootTest
class AgentServiceTest @Autowired constructor(val agentService: AgentService, val agentRepository: AgentRepository,
                                              val userRepository: UserRepository, val mockMvc: MockMvc) {
    var agent = Agent().apply {

        this.address.houseNo = "7"
        this.address.street = "oyewole habeeb"
        this.address.city = "gbagada"
        this.user.username = "udubic@gmail.com"
        this.user.email = "udubic@gmail.com"
        this.user.name.firstName = "dubic"
        this.user.name.lastName = "uzuegbu"
        this.user.phoneNumber = "08032356801"
    }.also { it.agentId = agentService.buildAgentId(it) }

    @BeforeEach
    fun setUp() {
        agentRepository.deleteAll()
        userRepository.deleteAll()
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
        agentService.unique(agent, agent.user.id)
    }

    @Test
    fun `Agent Business Rules check`() {

    }

    @Test

    fun `Onboard Agent`() {
        this.mockMvc.perform(put("/agents/onboard")
                .content(Utils.toJson(agent))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk)
//                .andExpect(jsonPath("$.messages", hasSize(1)))
                .andExpect(jsonPath("$.messages[0].msgCode", `is`("success.agent.onboard")))

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
        agent.user.name.firstName = "dubine"
        agent.user.email = "dubine@updated.com"

        this.mockMvc.perform(patch("/agents/updateAgent")
                .content(Utils.toJson(agent))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk)
                .andExpect(jsonPath("$.messages[0].msgCode", `is`("success.agent.updated")))
                .andExpect(jsonPath("$.payload.user.name.firstName", `is`("dubine")))

        val agent = agentRepository.findById(agent.id!!).get()

        assertThat("Agent name was not updated", agent.user.name.firstName, `is`("dubine"))
        assertThat("Agent email was not updated", agent.user.email, `is`("dubine@updated.com"))
        assertThat("Agent user email was not updated", agent.user.email, `is`("dubine@updated.com"))
        //user login saved
        val user = userRepository.findByUsername(agent.user.username)
        assertThat("User email not updated", user?.email, `is`("dubine@updated.com"))
        assertThat("User firstname not updated", user?.name?.firstName, `is`("dubine"))
    }

    @Test
    fun `Delete Agent`() {
        agentRepository.save(agent)
        this.mockMvc.perform(delete("/agents/delete/${agent.id}")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk)
                .andExpect(jsonPath("$.messages[0].msgCode", `is`("success.agent.deleted")))
        val agents = agentRepository.findAll()
        assertThat("Agent was not deleted in DB", agents, hasSize(0))

        assertThat("User was not deleted in DB", userRepository.findAll(), hasSize(0))
    }

    @Test
    fun `Find Agent By Id`() {
        agentRepository.save(agent)
        this.mockMvc.perform(get("/agents/findById/${agent.id}")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk)
                .andExpect(jsonPath("$.payload.user.email", `is`(agent.user.email)))
    }

    @Test
    fun `List Agents`() {
        agentRepository.save(agent)
        this.mockMvc.perform(post("/agents/list")
                .content(Utils.toJson(agent))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk)
                .andExpect(jsonPath("$.payload[0].user.email", `is`(agent.user.email)))
    }


}