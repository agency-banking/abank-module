/**
 *
 */
package com.agencybanking.agents

import com.agencybanking.core.data.Active
import com.agencybanking.core.data.BaseEntity
import com.agencybanking.core.el.Label
import org.hibernate.annotations.Where
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

/**
 * @author dubic
 *
 */
@Active
@Label("Agent")
@Where(clause = "del_time IS NULL")
@Entity
@Table(name = "ag_agent")
class Agent(

        @Label("First Name")
        @NotBlank(message = "agents.agent.firstname.empty")
        @Size(min = 1, message = "agents.agent.firstname.size", max = 50)
        @Column(name = "first_nm", nullable = false)
        var firstName: String,

        @Label("Last Name")
        @Size(message = "agents.agent.lastname.size", min = 1, max = 50)
        @NotBlank(message = "agents.agent.lastname.empty")
        @Column(name = "last_nm", nullable = false)
        var lastName: String,

        @Label("Phone")
        @NotBlank(message = "agents.agent.phone.empty")
        @Size(message = "agents.agent.phone.size", max = 50)
        @Column(name = "phone", nullable = false)
        var phone: String

) : BaseEntity() {


    override fun product(): String {
        return "agent"
    }

    override fun module(): String {
        return AgentsModule.CODE
    }
}