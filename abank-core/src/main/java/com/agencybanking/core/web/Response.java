package com.agencybanking.core.web;

import com.agencybanking.core.services.AppContextHolder;
import com.agencybanking.core.web.messages.ErrorMessage;
import com.agencybanking.core.web.messages.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.agencybanking.core.web.messages.MessageType.*;

@Data
@Slf4j
public class Response {
    private final Object payload;
    @JsonIgnore
    private final MessageSource messageSource;
    private List<Message> messages = new ArrayList<>();

    private Response(Object t, MessageSource messageSource) {
        this.payload = t;
        this.messageSource = messageSource;
    }

    private Response(MessageSource messageSource) {
        this.messageSource = messageSource;
        this.payload = null;
    }

    /**
     * Add a warning message to the data's message list
     *
     * @param code   message code defined in module's properties file
     * @param params parameters to be resolved
     */
    public Response warn(String code, Object... params) {
        Message message = new Message(code, resolveMessage(code, params), WARNING);
        this.getMessages().add(message);
        return this;
    }

    /**
     * Add an error message to the data's message list
     *
     * @param code   message code defined in module's properties file
     * @param params parameters to be resolved
     */
    public Response error(String code, Object... params) {
        ErrorMessage message = new ErrorMessage(code, resolveMessage(code, params), ERROR);
        this.getMessages().add(message);
        return this;
    }

    /**
     * Add an info message to the data's message list
     *
     * @param code   message code defined in module's properties file
     * @param params parameters to be resolved
     */
    public Response info(String code, Object... params) {
        Message message = new Message(code, resolveMessage(code, params), INFO);
        this.getMessages().add(message);
        return this;
    }

    /**
     * Add a success message to the data's message list
     *
     * @param code   message code defined in module's properties file
     * @param params parameters to be resolved
     */
    public Response success(String code, Object... params) {
        Message message = new Message(code, resolveMessage(code, params), SUCCESS);
        this.getMessages().add(message);
        return this;
    }

    private String resolveMessage(String code, Object[] params) {
        try {
            HttpServletRequest request = AppContextHolder.getInstance().getRequest();
            return messageSource.getMessage(code, params, (request == null) ? Locale.ENGLISH : request.getLocale());
        } catch (NoSuchMessageException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    public static Response of(Object t, MessageSource messageSource) {
        return new Response(t, messageSource);
    }

    public static Response of(MessageSource messageSource) {
        return new Response(messageSource);
    }
}
