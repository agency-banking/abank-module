package com.agencybanking.core.data

import com.agencybanking.core.el.Label
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Embeddable
class Address {
    @Label("House Number")
    @NotBlank
    @Size(max = 100)
    @Column(name = "house_no")
    var houseNo: String = ""

    @Label("Street")
    @NotBlank
    @Size(max = 255)
    @Column(name = "street")
    var street: String = ""

    @Label("City")
    @Size(max = 100)
    var city: String = ""

    @Label("State")
    @Size(max = 100)
    var state: String = ""
}