package com.agencybanking.core.messaging;

import com.agencybanking.core.data.BaseEnum;
import com.agencybanking.core.web.lists.DataList;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@DataList
@XmlType(name = "messageType")
@XmlEnum(String.class)
public enum AppMessageType implements BaseEnum {

    EMAIL("Email"),
    WEB("Web"),
    SMS("SMS");

    private String description;

    AppMessageType(String description) {
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
}
