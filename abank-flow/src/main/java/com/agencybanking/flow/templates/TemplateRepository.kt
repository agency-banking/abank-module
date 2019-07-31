/**
 *
 */
package com.agencybanking.core.templates

import com.agencybanking.flow.templates.Template
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface TemplateRepository : JpaRepository<Template, Long>, QuerydslPredicateExecutor<Template> {
    fun findByName(name: String): Template?

    fun findByRef(ref: String): Template?

}