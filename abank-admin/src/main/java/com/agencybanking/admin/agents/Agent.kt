/**
 *
 */
package com.agencybanking.admin.agents

import com.agencybanking.admin.AdminModule
import com.agencybanking.core.data.Address
import com.agencybanking.core.data.BaseEntity
import com.agencybanking.core.data.Geolocation
import com.agencybanking.core.data.Name
import com.agencybanking.core.el.Label
import com.agencybanking.security.users.User
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.Email
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
    @Label("", embedded = true)
    @Embedded
    var name: Name = Name()

    @Label("Phone")
    @NotBlank(message = "admin.agent.phone.empty")
    @Size(message = "admin.agent.phone.size", max = 50)
    @Column(name = "phone", nullable = false, unique = true)
    var phone: String = ""

    @Label("Email")
    @Size(message = "admin.agent.email.size", max = 255)
    @Email(message = "admin.agent.email.email")
    @Column(name = "email", unique = true)
    var email: String = ""

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

    override fun forCode(): String {
        return phone + email
    }

    override fun product(): String {
        return "agent"
    }

    override fun module(): String {
        return AdminModule.CODE
    }

    fun fillInUser() {
        user.firstName = name.firstName
        user.lastName = name.lastName
        user.email = email
        user.phoneNumber = phone
    }
}