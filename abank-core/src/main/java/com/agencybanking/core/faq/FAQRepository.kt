package com.agencybanking.core.faq

import org.springframework.data.jpa.repository.JpaRepository

interface FAQRepository : JpaRepository<FAQ, Long> {
}