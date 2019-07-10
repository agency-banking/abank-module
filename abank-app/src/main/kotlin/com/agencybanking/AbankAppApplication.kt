package com.agencybanking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import com.agencybanking.core.data.AuditDetails
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware
import java.util.*


@EnableJpaAuditing
@SpringBootApplication
class AbankAppApplication {

    fun main(args: Array<String>) {
        runApplication<AbankAppApplication>(*args)
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
}