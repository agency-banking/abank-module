package com.agencybanking.core.web.messages;

import com.agencybanking.core.data.BaseEntity;
import com.agencybanking.core.utils.Utils;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "core_errors")
@SequenceGenerator(name = "defaultSequenceGen", initialValue = 1000, sequenceName = "CORE_ERRORS_SEQ", allocationSize = 1)
public class Message extends BaseEntity {
    private String msgCode;
    private String msg;
    @Transient
    private MessageType msgType = MessageType.WARNING;
    private String mainEx;
    private String trace;
    private String causeEx;
    @NotNull
    private Boolean fixed;
    @Lob
    private String form;

    public Message() {
    }

    public Message(String msg) {
        this.msg = msg;
    }

    public Message(String msg, MessageType type) {
        this(msg);
        this.msgType = type;
    }

    /**
     * @param fullCode e.g success.mbcp-201
     * @param msg
     * @param type
     */
    public Message(String fullCode, String msg, MessageType type) {
        this(msg, type);
        String[] codes = fullCode.split("\\.");
        if (codes.length > 1) {
            this.msgCode = codes[1].toUpperCase();
        } else {
            this.msgCode = fullCode;
        }
    }

    public Message exception(Throwable e) {
        this.mainEx = Utils.first(e.getClass().getName() + ":" + e.getMessage(), 255);
        Throwable cause = e.getCause();
        if (cause != null) {
            this.causeEx = Utils.first(cause.getClass().getName() + ":" + cause.getMessage(), 255);
        }
        int giveup = 0;
        for (StackTraceElement stack : e.getStackTrace()) {
            if (stack.getClassName().startsWith(PACKAGE) || giveup == 20) {
                this.trace = String.format("%s.%s(%d)", stack.getClassName(), stack.getMethodName(), stack.getLineNumber());
                System.out.println("STACK DESIRED GOTTEN : " + this.trace);
                break;
            }
            giveup++;
        }
        return this;
    }
}
