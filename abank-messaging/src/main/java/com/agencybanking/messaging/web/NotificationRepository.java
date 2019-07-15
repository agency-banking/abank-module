package com.agencybanking.messaging.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findBySeenDateIsNullAndReadDateIsNull(Pageable pageable);
    Page<Notification> findByUser(String user, Pageable pageable);
    Long countBySeenDateIsNull();
}
