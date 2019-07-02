package com.agencybanking.core.faq

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "core_faq")
data class FAQ(
        @Id
        val question: String,

        @Column(name = "ans", length = 500)
        val answer: String
) {
}