/**
 *
 */
package com.agencybanking.core.data

import com.agencybanking.core.utils.BeanUtil
import org.springframework.data.domain.AuditorAware
import java.util.*
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

/**
 * @author dubic
 */
class SystemEntityListener {

     val auditDetails: AuditDetails
        get() {
            val auditor = BeanUtil.getBean(AuditorAware::class.java) as AuditorAware<AuditDetails>
            return auditor.currentAuditor.get()
        }

    @PrePersist
    fun touchForCreate(data: SystemEntity) {
        val createDate = Date()
        data.createDate = createDate
        data.modifiedBy = auditDetails.createdBy
        data.modifiedDate = createDate
    }

    @PreUpdate
    fun touchForUpdate(data: SystemEntity) {
        data.modifiedDate = Date()
        val auditDetails = auditDetails
        data.modifiedBy = auditDetails.createdBy
    }
}
