package com.agencybanking.security.users

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "sec_user_pers")
class UserPermission {
    @Id
    val id: Long? = null
    val CAN_EDIT = "CAN_CREATE"

    var username: String = ""
    var authority: String = ""
}