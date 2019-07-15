package com.agencybanking.core.templates

import java.util.*

interface TemplateProvider {


    /**Resolve a simple text recipients string
     * @param text
     * @param variables
     * @return the resolved string or same string if no templates in it
     */
    fun resolveText(text: String, variables: Map<String, Any>): String

    /**Resolve a template recipients string using.
     * @param templateRef the ID of the template
     * @param variables
     * @return resolved string or null if no template with templateId is found
     */
    fun resolveTemplate(templateRef: String, variables: Map<String, Any>): String
}