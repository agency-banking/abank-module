package com.agencybanking.core.services;

import com.agencybanking.core.data.Data;
import com.agencybanking.core.web.messages.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static com.agencybanking.core.web.messages.MessageType.*;

@Slf4j
public abstract class BaseService {
    @Autowired
    private MessageSource messageSource;

    public String resolveMessage(String code, Object[] params) {
        try {
            HttpServletRequest request = AppContextHolder.getInstance().getRequest();
            return messageSource.getMessage(code, params, (request == null) ? Locale.ENGLISH : request.getLocale());
        } catch (NoSuchMessageException e) {
            log.warn(e.getMessage());
        }
        return code;
    }
}
