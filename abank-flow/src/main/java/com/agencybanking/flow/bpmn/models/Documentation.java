package com.agencybanking.flow.bpmn.models;

import lombok.Data;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlValue;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Documentation {
    @XmlCDATA
    @XmlValue
    private String description;

    public Documentation() {
    }

    public Documentation(String s) {
        this.description=s;
    }
}
