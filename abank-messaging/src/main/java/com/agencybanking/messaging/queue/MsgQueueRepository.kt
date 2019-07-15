package com.agencybanking.messaging.queue

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MsgQueueRepository : JpaRepository<MsgQueueData, Long> {
    @Query("select m from MsgQueueData m where CURRENT_TIMESTAMP > m.sendDate and m.sendStatus in ?1")
    fun findUnsent(state: List<QueueSendState>, page: PageRequest): Page<MsgQueueData>
}