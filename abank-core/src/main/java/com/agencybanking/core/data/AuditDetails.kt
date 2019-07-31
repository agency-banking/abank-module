/**
 *
 */
package com.agencybanking.core.data


import java.io.Serializable

/**
 * @author Goodluck
 */
class AuditDetails : Serializable {
    var createdBy: String? = null

    var cmpCode: String = "SYSTEM"

    var parentCmpCode: String = "SYSTEM"
    var companyType: String? = null
    var companyTIN: String? = null

    constructor(createdBy: String) {
        this.createdBy = createdBy
    }
    constructor(createdBy: String, companyCode: String) {
        this.createdBy = createdBy
        this.cmpCode = companyCode
    }

    constructor(createdBy: String, companyCode: String, parentCode: String) {
        this.createdBy = createdBy
        this.cmpCode = companyCode
        this.parentCmpCode = parentCode
    }
}
