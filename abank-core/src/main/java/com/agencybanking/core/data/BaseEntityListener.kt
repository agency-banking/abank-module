/**
 *
 */
package com.agencybanking.core.data

import com.agencybanking.core.utils.BeanUtil
import com.agencybanking.core.utils.Utils
import org.springframework.data.domain.AuditorAware
import org.springframework.util.ObjectUtils
import java.util.*
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

/**
 * @author dubic
 */
class BaseEntityListener {

    private val auditDetails: AuditDetails
        get() {
            val auditor = BeanUtil.getBean(AuditorAware::class.java) as AuditorAware<AuditDetails>
            return auditor.currentAuditor.get()
        }

    @PrePersist
    fun touchForCreate(baseEntity: BaseEntity) {
        val createDate = Date()
        baseEntity.createDate = createDate
        val auditDetails = auditDetails
        baseEntity.createdBy = auditDetails.createdBy

        baseEntity.modifiedBy = auditDetails.createdBy
        baseEntity.modifiedDate = createDate
        //set active flag based on annotation
        val activeAnnot = baseEntity.javaClass.getDeclaredAnnotation(Active::class.java)
        baseEntity.active = activeAnnot != null && activeAnnot.flag
        //set code
        baseEntity.code = Utils.hash(baseEntity.forCode())
        println("Set Code ::: ${baseEntity.code}")
        //multitenancy
        if(BaseTenantEntity::class.java.isAssignableFrom(baseEntity.javaClass)){
            baseEntity as BaseTenantEntity
            if (ObjectUtils.isEmpty(baseEntity.cmpCode)) {
                baseEntity.cmpCode = auditDetails.cmpCode
            }
            if (ObjectUtils.isEmpty(baseEntity.parentCmpCode)) {
                baseEntity.parentCmpCode = auditDetails.parentCmpCode
            }
        }
    }

    @PreUpdate
    fun touchForUpdate(data: BaseEntity) {
        data.modifiedDate = Date()
        val auditDetails = auditDetails
        data.modifiedBy = auditDetails.createdBy
    }
}
