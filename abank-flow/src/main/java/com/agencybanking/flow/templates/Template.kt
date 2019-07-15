/**
 *
 */
package com.agencybanking.flow.templates

import com.agencybanking.core.data.Active
import com.agencybanking.core.data.BaseEntity
import com.agencybanking.core.el.Label
import com.agencybanking.core.messaging.AppMessageType
import com.agencybanking.flow.FlowModule
import org.springframework.util.Assert
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * @author dubic
 */
@Label("Template")
@Active
@Entity
@Table(name = "msg_tpl", indexes = arrayOf(Index(name = "ref_index", columnList = "ref")))
@SequenceGenerator(name = "defaultSequenceGen", initialValue = 1000, sequenceName = "BIZ_MSG_TPL_SEQ", allocationSize = 1)
class Template : BaseEntity() {

    @Label("Name")
    @NotBlank(message = "business.template.name.empty")
    @Size(max = 50, message = "business.template.name.size")
    @Column(name = "name", nullable = false)
    var name: String = ""

    @Label("Subject")
    @Size(message = "business.template.subject.size", max = 255)
    @Column(name = "subject")
    var subject: String? = null

    @Label("Text")
    @Lob
    @NotBlank(message = "business.template.text.empty")
    @Column(name = "text", nullable = false)
    var text: String = ""

    @Label("Type")
    @NotNull(message = "business.template.messageType.required")
    @Column(name = "type_", nullable = false)
    @Enumerated(EnumType.STRING)
    var type: AppMessageType = AppMessageType.EMAIL

    @Label("Module")
    @Size(max = 255, message = "business.template.module.size")
    @Column(name = "module")
    var module: String? = null

    @Label("Product")
    @Size(message = "business.template.product.size", max = 255)
    @Column(name = "product")
    var product: String = ""

    @Label("Tenant")
    @NotNull(message = "business.template.tenant.empty")
    @Column(name = "tenant", nullable = false)
    var tenant: String? = null

    @Label("TxnReference")
    @Size(max = 255, message = "core.template.ref.size")
    @NotBlank(message = "core.template.ref.empty")
    @Column(name = "ref", nullable = false, updatable = false, unique = true)
    private var ref: String = ""

    override fun product(): String {
        return "template"
    }

    override fun module(): String {
        return FlowModule.CODE
    }

    override fun forCode(): String {
        return this.name + this.tenant
    }

    fun buildRef(): String {
        Assert.hasLength(this.name, "Template name is needed for ref")
        Assert.notNull(this.tenant, "Tenant is needed for ref")
        this.ref = (this.name.replace(" ".toRegex(), "_") + "_" + this.tenant).toLowerCase()
        return this.ref
    }
}