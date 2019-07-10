package com.agencybanking.core.services;

import com.agencybanking.core.web.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

@Slf4j
public abstract class BaseController {
    @Autowired
    private MessageSource messageSource;


    public Response respondWith(Object t) {
        return Response.of(t, messageSource);
    }
}
