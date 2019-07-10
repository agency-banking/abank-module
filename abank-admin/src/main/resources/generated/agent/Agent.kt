/**
 * 
 */
package com.agencybanking.agents

import javax.persistence.Entity
import javax.persistence.Column
import javax.validation.constraints.Size
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import com.agencybanking.core.el.Label
import com.querydsl.core.BooleanBuilder
import com.agencybanking.core.data.SearchRequest
import javax.validation.constraints.Email
import com.agencybanking.core.data.BaseEntity
import javax.validation.constraints.NotNull

/**
 * @author dubic 
 *
 */

@Label("Agent")


@Entity
@Table(name = "ag_agent")
class Agent (

        @Label("First Name")
		@NotBlank(message="agents.agent.firstname.empty")
		@Size(min=1,message="agents.agent.firstname.size",max=50)
        @Column(name="first_nm",nullable=false)
        var firstName: String,

        @Label("Last Name")
		@Size(message="agents.agent.lastname.size",min=1,max=50)
		@NotBlank(message="agents.agent.lastname.empty")
        @Column(name="last_nm",nullable=false)
        var lastName: String,

        @Label("Phone")
		@NotBlank(message="agents.agent.phone.empty")
		@Size(message="agents.agent.phone.size",max=50)
        @Column(name="phone",nullable=false)
        var phone: String,

        @Label("Email")
		@Size(message="agents.agent.email.size",max=255)
		@Email(message="agents.agent.email.email")
        @Column(name="email")
        var email: String,

        @Label("Geolocation")
		@Size(message="agents.agent.geolocation.size",max=255)
        @Column(name="geo")
        var geolocation: String,

        @Label("Address")
		@Size(message="agents.agent.address.size",max=255)
        @Column(name="address")
        var address: String,

        @Label("BVN")
		@Size(message="agents.agent.bvn.size",max=255)
        @Column(name="bvn")
        var bvn: String,

        @Label("Parent")
        @Column(name="parent")
        var parent: Long,

        @Label("Super")
		@NotNull(message="agents.agent.super.required")
        @Column(name="super_",nullable=false)
        var super: boolean,

        @Label("User Id")
		@NotNull(message="agents.agent.userid.required")
		@Size(message="agents.agent.userid.size",max=255)
        @Column(name="user_id",nullable=false)
        var userId: String

    ): BaseEntity() {


	override fun forCode(): String {
        return phone+email
    }

    override fun product(): String {
        return "agent"
    }

    override fun module(): String {
        return AgentsModule.CODE
    }
}