package com.agencybanking.flow.audits

import com.agencybanking.core.data.DataViews
import com.agencybanking.core.services.BizEvent
import com.agencybanking.core.utils.Utils
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.util.ObjectUtils
import java.util.*
import java.util.function.Supplier

/**
 * @author dubic
 */
@Service
@Slf4j
open class AuditService @Autowired constructor(val auditRepository: AuditRepository, val ioAuditRepository: IOAuditRepository) {
    private val log = LoggerFactory.getLogger(this.javaClass)
    @Async
    open fun log(user: String, ev: BizEvent) {
        val audit = Audit().apply {
            this.module = ev.module
            this.action = ev.action
            this.product = ev.product
            this.data = Utils.toJson(ev.getValues(), DataViews.Audits::class.java, true)
            this.user = user
            this.date = Date()
            this.ref = ev.id
        }
        val saved = this.auditRepository.save<Audit>(audit)
        log.info(saved.toJsonString(false))
    }

    @Async
    open fun logIO(ioAuditSupplier: Supplier<IOAudit>) {
        val saved = this.ioAuditRepository.save(ioAuditSupplier.get())
        log.info(saved.toJsonString(false))
    }

    //	public void resent()

    /**Creates a new record of Audit
     *
     * @param audit like from new ()
     * @return the created record
     */
    fun create(audit: Audit): Audit {
        throw UnsupportedOperationException("Method N/A")
    }

    fun update(audit: Audit): Audit {
        throw UnsupportedOperationException("Method N/A")
    }

    fun delete(audit: Audit) {
        auditRepository.delete(audit)
    }

    fun findById(id: Long?): Optional<Audit> {
        return auditRepository.findById(id!!)
    }

    fun query(audit: Audit, p: PageRequest): Page<Audit> {
        return if (ObjectUtils.isEmpty(audit)) {
            auditRepository.findAll(p)
        } else auditRepository.findAll(Example.of(audit), p)
    }

    fun exists(audit: Audit, id: Long?): Boolean {
        return auditRepository.exists(Example.of(audit))
    }

//    fun findAll(audit: Audit, p: PageRequest): Page<Audit> {
//        return auditRepository.findAll(audit.predicates(), p)
//    }
//
//    fun singleSearch(request: SearchRequest, p: PageRequest): Page<Audit> {
//        return auditRepository.findAll(Audit().anyPredicates(request), p)
//    }

//    fun single(vararg args: String): Page<Audit>? {
//        return null
//    }
//
//    fun ioFindAll(ioAudit: IOAudit, pageRequest: PageRequest): Page<IOAudit> {
//        return ioAuditRepository.findAll(ioAudit.predicates(), pageRequest)
//    }

//    fun ioSingleSearch(request: SearchRequest, p: PageRequest): Page<IOAudit> {
//        return ioAuditRepository.findAll(IOAudit().anyPredicates(request), p)
//    }
//
//    fun currentActivities(request: SearchRequest, p: PageRequest): List<Any> {
//        val activities = ArrayList<Any>()
//        for (audit in ioSingleSearch(request, p)) {
//            // activities.add(Utils.unmarshall(audit.getData(), ServiceRequest.class));
//            activities.add(Utils.unmarshall(audit.getData(), ServiceResponse::class.java))
//        }
//        //System.out.println(activities);
//        return activities
//    }
}
