package com.agencybanking.flow.bpmn.models;

import lombok.Data;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlValue;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ConditionExpression {
    @XmlCDATA
    @XmlValue
    private String expression;

    public ConditionExpression() {
    }

    public ConditionExpression(String s) {
        this.expression=s;
    }
}
