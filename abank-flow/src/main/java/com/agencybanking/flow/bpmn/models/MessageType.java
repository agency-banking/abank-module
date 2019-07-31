package com.agencybanking.flow.bpmn.models;

import com.agencybanking.core.data.BaseEnum;
import com.agencybanking.core.web.lists.DataList;
import com.agencybanking.core.web.messages.MessageTypes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import java.util.Arrays;

@DataList
@XmlType(name = "messageType")
@XmlEnum(String.class)
public enum MessageType implements BaseEnum {

    EMAIL("Email"),
    WEB("Web"),
    SMS("SMS");

    private String description;

    MessageType(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name();
    }

    public static MessageTypes fromValue(String val) {
        return Arrays.stream(MessageTypes.values())
                .filter(l -> l.getDescription().equalsIgnoreCase(val))
                .findFirst().orElse(null);
    }
}
