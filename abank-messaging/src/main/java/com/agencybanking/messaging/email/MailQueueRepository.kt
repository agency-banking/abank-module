package com.agencybanking.messaging.email

import com.agencybanking.messaging.queue.QueueSendState
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MailQueueRepository : JpaRepository<MailQueueData, Long> {
    @Query("select m from MailQueueData m where CURRENT_TIMESTAMP > m.sendDate and m.sendStatus in ?1")
    fun findUnsent(state: List<QueueSendState>, page: PageRequest): Page<MailQueueData>
}