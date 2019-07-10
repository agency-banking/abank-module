package com.agencybanking.core.data

import com.agencybanking.core.el.Label
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Embeddable
class Name {
    @Label("First Name")
    @NotBlank(message = "firstname.empty")
    @Size(min = 1, message = "firstname.size", max = 50)
    @Column(name = "first_nm", nullable = false)
    var firstName: String = ""

    @Label("Last Name")
    @Size(message = "lastname.size", min = 1, max = 50)
    @NotBlank(message = "lastname.empty")
    @Column(name = "last_nm", nullable = false)
    var lastName: String = ""

    fun fullName() = "$firstName $lastName"
    fun fullNameOfficaial() = "$lastName $firstName"
}