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
@EntityListeners(BaseEntityListener::class)
@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
open class BaseEntity : Data(), Codeable {

    @XmlTransient
    @Id
    @SequenceGenerator(name = "defaultSequenceGen", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "defaultSequenceGen")
    var id: Long? = null

    @XmlTransient
    @Column(name = "active", nullable = false)
    var active: Boolean = false

    @XmlTransient
    @Label("Creator")
    @Column(name = "created_by", nullable = false, updatable = false)
    var createdBy: String? = null

    @XmlTransient
    @LastModifiedBy
    @Column(name = "modified_by", nullable = false)
    var modifiedBy: String? = null

    @XmlTransient
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    var createDate: Date = Date()

    @XmlTransient
    @LastModifiedDate
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    var modifiedDate: Date? = null

    @Version
    @XmlTransient
    var version: Long? = null

    @XmlTransient
    @Column(name = "code", nullable = false, unique = true, updatable = false)
    var code: String? = null


    @XmlTransient
    @Transient
    var fromDate: Date? = null

    @XmlTransient
    @Transient
    var toDate: Date? = null

    open fun product(): String {
        throw UnsupportedOperationException("Kindly override BaseEntity#product() in " + BaseEntity::class.simpleName)
    }

    open fun module(): String {
        throw UnsupportedOperationException("Kindly override BaseEntity#module() in " + BaseEntity::class.simpleName)
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (BaseEntity::class !== obj!!.javaClass)
            return false
        val other = obj as BaseEntity?
        if (this.id == null) {
            if (other!!.id != null)
                return false
        } else if (this.id != other!!.id)
            return false
        return true
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (this.id == null) 0 else this.id!!.hashCode()
        return result
    }


    override fun forCode(): String {
        return UUID.randomUUID().toString()
    }
}
