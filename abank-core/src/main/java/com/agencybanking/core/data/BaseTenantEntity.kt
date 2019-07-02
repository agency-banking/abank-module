package com.agencybanking.core.data

/**
 *
 */

import com.agencybanking.core.el.Label
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlTransient

/**
 * @author dubic
 */
@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
open class BaseTenantEntity : BaseEntity() {

    @XmlTransient
    @Column(name = "creator_cmp_cd", nullable = false, updatable = false)
    var cmpCode: String? = null
        set(cmpCode) {
            field = this.cmpCode
        }

    @XmlTransient
    @Column(name = "parent_cmp_cd", nullable = false, updatable = false)
    var parentCmpCode: String? = null
        set(parentCmpCode) {
            field = this.parentCmpCode
        }
}
