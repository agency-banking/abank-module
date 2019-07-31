/**
 *
 */
package com.agencybanking.admin.agents

import com.agencybanking.admin.AdminModule
import com.agencybanking.core.data.Address
import com.agencybanking.core.data.BaseEntity
import com.agencybanking.core.data.Geolocation
import com.agencybanking.core.el.Label
import com.agencybanking.core.services.BizServiceException
import com.agencybanking.security.users.User
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * @author dubic
 *
 */
@Label("Agent")
@Entity
@Table(name = "adm_agent")
class Agent : BaseEntity() {

    @Valid
    @Label("Geolocation")
    var geolocation: Geolocation? = null

    @Valid
    @Label("Address", embedded = true)
    @NotNull
    @Embedded
    var address: Address = Address()

    @Label("BVN")
    @Size(message = "admin.agent.bvn.size", max = 255)
    @Column(name = "bvn")
    var bvn: String? = null

    @Label("Parent")
    @Column(name = "parent")
    var parent: Long? = null

    @Label("Super")
    @Column(name = "super_", nullable = false)
    var isSuper: Boolean = false

    @Label("User")
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", nullable = false)
    var user: User = User()

    @Label("Agent Id")
    @Size(message = "admin.agent.id.size", max = 255)
    @NotBlank(message = "admin.agent.id.blank")
    @Column(name = "agent_id", unique = true, nullable = false, updatable = false)
    var agentId: String? = null

    override fun forCode(): String {
        return user.email + user.name.fullName() + System.currentTimeMillis()
    }

    override fun product(): String {
        return "agent"
    }

    override fun module(): String {
        return AdminModule.CODE
    }

    fun hasEmail(): Agent {
        if (user.email.isBlank()) {
            throw BizServiceException(AdminModule.ERR_EMAIL_REQUIRED)
        }
        return this
    }

    fun hasPhone(): Agent {
        if (user.phoneNumber.isBlank()) {
            throw BizServiceException(AdminModule.ERR_PHONE_REQUIRED)
        }
        return this
    }

}