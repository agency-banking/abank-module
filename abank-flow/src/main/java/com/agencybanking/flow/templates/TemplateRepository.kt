/**
 *
 */
package com.agencybanking.core.templates

import com.agencybanking.flow.templates.Template
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

import java.util.Optional

interface TemplateRepository : JpaRepository<Template, Long>, QuerydslPredicateExecutor<Template> {
    fun findByNameAndTenantId(name: String, tenantId: Long?): Template?
    //    Optional<Template> findByNameAndTenant(String name, Long tenantId);


    fun findByNameAndTenantIdAndIdNot(name: String, tenantId: Long?, id: Long?): Template?

    fun findByRef(ref: String): Template?

}