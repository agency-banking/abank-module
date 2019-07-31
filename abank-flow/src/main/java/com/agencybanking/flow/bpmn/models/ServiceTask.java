package com.agencybanking.flow.bpmn.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author dubic
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTask extends BPMNNode {

    @XmlAttribute(name = "expression")
    private String expression;

    @XmlAttribute(name = "resultVariable")
    private String resultVariable;
}
