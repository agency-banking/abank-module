package com.agencybanking.core.services;

import com.agencybanking.core.data.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
public abstract class BaseService {
    @Autowired
    private ApplicationEventPublisher publisher;

    public void broadcast(BaseEntity entity, String action) {
        this.publisher.publishEvent(
                BizEvent.of(entity).ref(entity.getId()).module(entity.module()).product(entity.product()).action(action)
                        .fireApproval(false));
    }
}
