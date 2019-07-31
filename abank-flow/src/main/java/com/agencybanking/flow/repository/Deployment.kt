package com.agencybanking.flow.repository

import com.agencybanking.core.data.Data
import com.agencybanking.flow.bpmn.models.ProcessDef
import com.agencybanking.flow.bpmn.models.ProcessDefinition
import org.springframework.core.io.Resource
import org.springframework.util.ObjectUtils
import org.xml.sax.SAXException
import java.io.IOException
import java.io.StringReader
import javax.persistence.*
import javax.validation.ConstraintViolationException
import javax.validation.constraints.NotEmpty
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

@Entity
@Table(name = "AF_PROC_DEP")
@SequenceGenerator(name = "defaultSequenceGen", initialValue = 1000, sequenceName = "AF_PROC_DEP_SEQ", allocationSize = 1)
class Deployment : Data(){
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "defaultSequenceGen")
    var id: Long? = null

    @Column(name = "active", nullable = false)
    var active: Boolean = false

    @Column(name = "nm", length = 100)
    var name: String = ""

    @NotEmpty
    @Column(name = "process_id", length = 255, nullable = false)
    var processId: String = ""

    @NotEmpty
    @Column(name = "tenant_id", length = 255)
    var tenantId: String? = null

    @Lob
    @NotEmpty
    @Column(name = "definition", nullable = false)
    var definitions: String = ""

    @Column(name = "has_apprv")
    var hasApproval: Boolean = false

    @Column(name = "mod")
    var module: String? = null

    @Column(name = "prod", nullable = false)
    var product: String = ""

    @Column(name = "event", nullable = false)
    var event: String = ""

    @Column(name = "desc_")
    var description: String? = null

    @Lob
    @Column(name = "diag")
    var diagram: String? = null

    @Transient
    var processDef: ProcessDef? = null


    fun getHasApproval(): Boolean? {
        return if (hasApproval == null) java.lang.Boolean.valueOf(false) else hasApproval
    }

    @Throws(ConstraintViolationException::class)
    fun validate(xsdResource: Resource) {
        super.validate()
        //        validateXMLSchema(xsdResource);
    }

    fun validateXMLSchema(xsdPath: Resource): Boolean {
        try {
            val factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
            val schema = factory.newSchema(xsdPath.url)
            val validator = schema.newValidator()
            val reader = StringReader(this.definitions)
            validator.validate(StreamSource(reader))
        } catch (e: IOException) {
            println("Exception: " + e.message)
            return false
        } catch (e1: SAXException) {
            println("SAX Exception: " + e1.message)
            return false
        }

        return true
    }

    fun hasApprovalTask(processDefinition: ProcessDefinition) {
        val userTasks = processDefinition.process.userTasks
        this.hasApproval = !ObjectUtils.isEmpty(userTasks)
    }
}