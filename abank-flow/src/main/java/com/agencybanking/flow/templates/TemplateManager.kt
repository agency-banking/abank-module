package com.agencybanking.messaging.templates

import com.agencybanking.core.templates.TemplateProvider
import com.agencybanking.core.templates.TemplateRepository
import freemarker.cache.StringTemplateLoader
import freemarker.template.Configuration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import java.io.StringWriter

class TemplateManager: TemplateProvider {

    private val log = LoggerFactory.getLogger(this.javaClass)
    @Autowired
    lateinit var tplRepository: TemplateRepository
    @Autowired
    lateinit var cfg: Configuration

    override fun resolveTemplate(templateRef: String, variables: Map<String, Any>): String {
        log.debug("resolving template ref... [{}]", templateRef)
        if (templateRef.isBlank()) {
            return templateRef
        }
        val t = tplRepository.findByRef(templateRef)?: throw IllegalArgumentException("Resource not found")
        return resolveText(t.text, variables)
    }

    override fun resolveText(text: String, variables: Map<String, Any>): String {
        log.debug("resolving text... [{}]", text)
        if (text.isBlank()) {
            return text
        }
        try {
            (cfg.templateLoader as StringTemplateLoader).putTemplate("plainText", text)
            val tpl = cfg.getTemplate("plainText")
            val out = StringWriter()
            tpl.process(variables, out)
            cfg.removeTemplateFromCache("plainText")
            //            log.debug("[{}]", out.toString());
            return out.toString()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}