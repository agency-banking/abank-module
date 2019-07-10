/**
 *
 */
package com.agencybanking.core.data

import com.agencybanking.core.validation.ValidationUtils
import com.agencybanking.core.web.messages.Message
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.BeanUtils
import org.springframework.util.StringUtils
import java.io.*
import java.util.ArrayList
import javax.validation.ConstraintViolationException
import javax.xml.bind.JAXB

/**
 * @author dubic
 */
open class Data : Serializable, BeanValidated {

    override fun toString(): String {
        return toJsonString(false)
    }

    fun toXmlString(): String {
        val buf = StringBuffer()
        val os = StringWriter()
        JAXB.marshal(this, os)
        buf.append(os.toString())
        return buf.toString()
    }

    fun toJsonString(header: Boolean): String {
        val mapper = ObjectMapper()
        try {
            var res = ""
            if (header) {
                res = res + "@" + this.javaClass.name
            }
            res += mapper.writeValueAsString(this)
            return res
        } catch (e: JsonProcessingException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return ""
    }

    fun toPlainString(): String {
        try {
            val buf = StringBuffer()
            buf.append("ProcessDefinition{").append("\n")
            for (field in this.javaClass.getDeclaredFields()) {
                buf.append(field.getName()).append("=").append(field.get(this))
            }
            buf.append("}")
            return buf.toString()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return ""
    }

    override fun validateForErrors(): List<String> {
        return ValidationUtils.getErrors(this)
    }

    @Throws(ConstraintViolationException::class)
    override fun validate() {
        ValidationUtils.validate(this)
    }

    @Throws(IOException::class)
    fun serialize(): String {
        val w = ByteArrayOutputStream()
        val out = ObjectOutputStream(w)
        out.writeObject(this)
        return w.toString()
    }

    /**
     * copies the properties of the source into this object
     *
     * @param source           recipients copy from
     * @param ignoreProperties properties recipients ignore
     */
    fun copy(source: Any, vararg ignoreProperties: String) {
        BeanUtils.copyProperties(source, this, *ignoreProperties)
    }

    /**
     * copies the properties of the source into this object ignoring properties
     * in BaseEntity :
     * createDate,version,modifiedDate,id,active,createdBy,modifiedBy
     *
     * @param source
     * @param ignoreProperties additional properties you want recipients ignore
     */
    fun copyForUpdate(source: Any, vararg ignoreProperties: String) {
        val ignoreList = "createDate,version,modifiedDate,id,code,active,createdBy,modifiedBy,transactionRef,cmpCode,parentCmpCode"
        val ignores = StringUtils.concatenateStringArrays(ignoreProperties, ignoreList.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray())
        BeanUtils.copyProperties(source, this, *ignores!!)
    }

    companion object {
        val WILDCARD_ALL = "*"
        val LOCAL_CCY = "NGN"
        const val PACKAGE = "com.agencybanking"
    }

}
