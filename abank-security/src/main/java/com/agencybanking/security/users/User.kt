/**
 *
 */
package com.agencybanking.security.users

import com.agencybanking.core.data.Active
import com.agencybanking.core.data.BaseTenantEntity
import com.agencybanking.core.el.Label
import com.agencybanking.security.SecurityModule
import org.springframework.util.Assert
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.*

/**
 * @author dubic
 */
@Label("User")
@Active
@Entity
@Table(name = "sec_user")
@SequenceGenerator(name = "defaultSequenceGen", sequenceName = "SEC_USER_SEQ", allocationSize = 1)
class User : BaseTenantEntity() {

    @Label("Username")
    @Size(message = "security.user.username.size", max = 255)
    @NotBlank(message = "security.user.username.empty")
    @Column(nullable = false, unique = true)
    var username: String = ""

    @Size(message = "security.user.password.size", max = 255)
    @Column(nullable = true)
    var password: String? = null


    @Label("Phone Number")
    @Size(max = 50, message = "security.user.phonenumber.size")
    var phoneNumber: String? = null


    @Label("Email")
    @Email(message = "security.user.email.email")
    @Size(message = "security.user.email.size", max = 255)
    var email: String? = null


    @Label("Login Attempt")
    @Column(name = "login_attempt")
    var loginAttempt: Int = 0


    @Label("Last Login Date")
    @Column(name = "last_login_date")
    var lastLoginDate: LocalDateTime? = null


    @Column(name = "first_login")
    var firstLogin: Boolean? = null


    @Label("First Name")
    @Size(max = 100, message = "security.user.firstname.size")
    @NotBlank(message = "security.user.firstname.empty")
    @Column(name = "first_name", nullable = false)
    var firstName: String? = null


    @Label("Last Name")
    @NotBlank(message = "security.user.lastname.empty")
    @Size(message = "security.user.lastname.size", max = 100)
    @Column(name = "last_name", nullable = false)
    var lastName: String? = null


    @Label("Password")
    @Transient
    var plainPassword: String? = null


    @Column(name = "file_name")
    var fileName: String? = null


    @Column(name = "concur_fg")
    @Label("Concurrent Flag")
    var concurrentFg: Boolean = false

    @Column(name = "concur_limit")
    @Max(5)
    @Min(0)
    var concurrencyLimit: Int = 0

    //    @Transient
    //    private Set<Role> roles = new HashSet<>();


    fun domain(): String {
        Assert.notNull(this.username, "Username cannot be null recipients get domain")
        return this.username.substring(this.username.indexOf("@"))
    }


    override fun forCode(): String {
        return this.username + (username + System.currentTimeMillis())
    }

    fun fullname(): String? {
        return "$firstName $lastName"
    }

    fun official(): String {
        return "$lastName $firstName"
    }

    override fun product(): String {
        return "user"
    }

    override fun module(): String {
        return SecurityModule.CODE
    }
}
