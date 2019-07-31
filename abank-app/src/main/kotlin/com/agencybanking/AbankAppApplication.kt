package com.agencybanking

import com.agencybanking.core.data.AuditDetails
import com.agencybanking.messaging.MessagingModule
import com.agencybanking.security.SecurityModule
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*


@EnableJpaAuditing
@EnableConfigurationProperties(value = [MessagingModule::class, SecurityModule::class])
@SpringBootApplication
class AbankAppApplication : CommandLineRunner {
    override fun run(vararg args: String?) {
    }

    @Bean
    fun auditorAware(): AuditorAware<AuditDetails> {
        return SecurityAuditAwareImpl()
    }

    class SecurityAuditAwareImpl : AuditorAware<AuditDetails> {

        override fun getCurrentAuditor(): Optional<AuditDetails> {
//        return Optional.of(AuthenticationService.currentAuditDetails())
            return Optional.of(AuditDetails("SYSTEM", "SYSTEM"))
        }

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<AbankAppApplication>(*args)
        }
    }
}