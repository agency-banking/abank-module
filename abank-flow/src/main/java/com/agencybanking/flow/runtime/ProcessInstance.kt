package com.agencybanking.flow.runtime

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "AF_RU_PROCINST")
class ProcessInstance {
    @Id
    var  id: Long? = null

    @Column(name = "process_id", nullable = false)
    var  processId: String? = null

    @Column(name = "tenant_id", nullable = false)
    var  tenantId: String? = null

    @Column(name = "active_node")
    var  activeNode: String? = null

    @Column(name = "suspended")
    var  suspended = false

    @Column(name = "completion")
    var  completion = 0

    @Column(name = "start_time")
    var  startTime = Date()

    @Column(name = "task_count")
    var  taskCount = 0

    @Column(name = "var_count")
    var  varCount = 0

    @Transient
    var  hasApproval: Boolean = false
}