package com.agencybanking.core.data

/**
 *
 */

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate

import javax.persistence.*
import java.util.Date

/**
 * @author dubic
 */
@EntityListeners(SystemEntityListener::class)
@MappedSuperclass
class SystemEntity : Data() {

    @Id
    @SequenceGenerator(name = "defaultSequenceGen", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "defaultSequenceGen")
    var id: Long? = null
        set(id) {
            field = this.id
        }

    @LastModifiedBy
    @Column(name = "modified_by", nullable = false)
    var modifiedBy: String? = null
        set(modifiedBy) {
            field = this.modifiedBy
        }

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    var createDate: Date? = null
        set(createDate) {
            field = this.createDate
        }

    @LastModifiedDate
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    var modifiedDate: Date? = null
        set(modifiedDate) {
            field = this.modifiedDate
        }

    @Version
    var version: Long? = null
        set(version) {
            field = this.version
        }

    @Transient
    var fromDate: Date? = null
        set(fromDate) {
            field = this.fromDate
        }
    @Transient
    var toDate: Date? = null
        set(toDate) {
            field = this.toDate
        }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (this::javaClass !== obj.javaClass)
            return false
        val other = obj as SystemEntity?
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
}
