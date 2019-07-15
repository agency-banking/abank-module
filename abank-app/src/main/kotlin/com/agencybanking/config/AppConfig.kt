package com.agencybanking.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
class AppConfig {
    @Bean
    fun messageSource(): MessageSource {
        val ms = ResourceBundleMessageSource()
        ms.setCacheMillis(60000)
        ms.setBasenames("core_messages", "admin_messages")
        return ms
    }
}