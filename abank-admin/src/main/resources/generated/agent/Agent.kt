/**
 * 
 */
package com.agencybanking.admin

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
class Agent : BaseEntity() {

        @Label("First Name")
		@Size(message="admin.agent.firstname.size",min=1,max=50)
		@NotBlank(message="admin.agent.firstname.empty")
        @Column(name="first_nm",nullable=false)
        var firstName: String

        @Label("Last Name")
		@NotBlank(message="admin.agent.lastname.empty")
		@Size(min=1,max=50,message="admin.agent.lastname.size")
        @Column(name="last_nm",nullable=false)
        var lastName: String

        @Label("Phone")
		@Size(message="admin.agent.phone.size",max=50)
		@NotBlank(message="admin.agent.phone.empty")
        @Column(name="phone",nullable=false)
        var phone: String

        @Label("Email")
		@Size(message="admin.agent.email.size",max=255)
		@Email(message="admin.agent.email.email")
        @Column(name="email")
        var email: String

        @Label("Geolocation")
		@Size(message="admin.agent.geolocation.size",max=255)
        @Column(name="geo")
        var geolocation: String

        @Label("Address")
		@Size(message="admin.agent.address.size",max=255)
        @Column(name="address")
        var address: String

        @Label("BVN")
		@Size(message="admin.agent.bvn.size",max=255)
        @Column(name="bvn")
        var bvn: String

        @Label("Parent")
        @Column(name="parent")
        var parent: Long

        @Label("Super")
		@NotNull(message="admin.agent.super.required")
        @Column(name="super_",nullable=false)
        var super: boolean

        @Label("User Id")
		@Size(message="admin.agent.userid.size",max=255)
		@NotNull(message="admin.agent.userid.required")
        @Column(name="user_id",nullable=false)
        var userId: String



	override fun forCode(): String {
        return phone+email
    }

    override fun product(): String {
        return "agent"
    }

    override fun module(): String {
        return AdminModule.CODE
    }
}