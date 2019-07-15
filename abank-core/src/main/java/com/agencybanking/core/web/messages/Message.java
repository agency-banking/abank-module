package com.agencybanking.core.web.messages;

import lombok.Data;

import javax.persistence.Transient;

@Data
public class Message  {
    protected String msgCode;
    protected String msg;
    @Transient
    protected MessageType msgType = MessageType.WARNING;


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
//        String[] codes = fullCode.split("\\.");
//        if (codes.length > 1) {
//            this.msgCode = codes[1].toUpperCase();
//        } else {
            this.msgCode = fullCode;
//        }
    }


}
