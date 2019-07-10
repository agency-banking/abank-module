package com.agencybanking.core.web.messages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MessageRepository extends JpaRepository<Message, Long>, QuerydslPredicateExecutor<Message> {
}
