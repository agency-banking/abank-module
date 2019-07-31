package com.agencybanking.flow.audits

import com.agencybanking.core.data.Data
import com.agencybanking.core.el.Label
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "af_audit", indexes = arrayOf(Index(name = "date_index", columnList = "create_date"), Index(name = "user_index", columnList = "user_id")))
class Audit: Data() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "af_aud_idGen")
    @SequenceGenerator(name = "af_aud_idGen", initialValue = 1000, sequenceName = "AF_AUDIT_SEQ", allocationSize = 1)
    var id: Long? = null

    @Transient
    var fromDate: Date? = null

    @Transient
    var toDate: Date? = null

    @Label("Date")
    @NotNull(message = "appflow.audit.date.required")
    @Column(name = "create_date", nullable = false)
    var date = Date()

    @Label("User")
    @NotBlank(message = "appflow.audit.user.empty")
    @Size(message = "appflow.audit.user.size", max = 255)
    @Column(name = "user_id", nullable = false)
    var user: String = ""

    @Lob
    @Column(name = "data")
    var data: String? = null

    @Label("Tenant")
    @Size(message = "appflow.audit.tenant.size", max = 255)
    @Column(name = "tenant")
    var tenant: String? = null

    @Label("Module")
    @Size(message = "appflow.audit.module.size", max = 255)
    @Column(name = "module")
    var module: String? = null

    @Label("Product")
    @Size(max = 255, message = "appflow.audit.product.size")
    @NotBlank(message = "appflow.audit.product.empty")
    @Column(name = "product", nullable = false)
    var product: String = ""

    @Label("Action")
    @Size(max = 255, message = "appflow.audit.action.size")
    @NotBlank(message = "appflow.audit.action.empty")
    @Column(name = "action", nullable = false)
    var action: String = ""

    @Label("Data ID")
    @Column(name = "ref")
    var ref: Long? = null
}