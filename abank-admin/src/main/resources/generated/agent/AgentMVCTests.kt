package com.agencybanking.admin

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
class AgentServiceTest @Autowired constructor(val agentService: agentService, val agentRepository: AgentRepository,
                                              val userRepository: UserRepository, val mockMvc: MockMvc) {
    var class com.unionsystems.springboard.models.Model = class com.unionsystems.springboard.models.Model()
            .also { sett values }

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
        //assertThat(assertThrows.messages.keys, hasSize(2))
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

    fun `Create Agent`() {
        this.mockMvc.perform(put("/agents/create")
            .content(Utils.toJson(agent))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk)
    //                .andExpect(jsonPath("$.messages", hasSize(1)))
            .andExpect(jsonPath("$.messages[0].msgCode", `is`("success.agent.create")))

        val agents = agentRepository.findAll()

        assertThat("Agent was not saved in DB", agents, hasSize(1))
        assertThat("Agent was not given id", agents[0].id, `is`(notNullValue()))
    }

    @Test
    fun `Update Agent Info`() {
        agentRepository.save(agent)
        //set diff values here

        this.mockMvc.perform(patch("/agents/updateAgent")
        .content(Utils.toJson(agent))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk)
        .andExpect(jsonPath("$.messages[0].msgCode", `is`("success.agent.updated")))

        val agent = agentRepository.findById(agent.id!!).get()
    //assert
    }

    @Test
    fun `Delete Agent`() {
        agentRepository.save(agent)
        this.mockMvc.perform(delete("/agents/delete/${agent.id}")
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk)
            .andExpect(jsonPath("$.messages[0].msgCode", `is`("success.agent.deleted")))
        val agents = agentRepository.findAll()
        assertThat("Agent was not deleted in DB", agents, hasSize(0))

    }

    @Test
    fun `Find Agent By Id`() {
        agentRepository.save(agent)
        this.mockMvc.perform(get("/agents/findById/$r{agent.id}")
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk)
            //.andExpect(jsonPath("$.payload.email", `is`(agent.email)))
    }

    @Test
    fun `List Agents`() {
        agentRepository.save(agent)
        this.mockMvc.perform(post("/agents/list")
            .content(Utils.toJson(agent))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk)
            //.andExpect(jsonPath("$.payload[0].email", `is`(agent.email)))
    }

}