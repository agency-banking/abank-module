package com.agencybanking.flow.bpmn.models;

import lombok.Data;

/**
 *
 */
@Data
public class StartEvent extends BPMNNode {

    public StartEvent() {
    }

    public StartEvent(String id) {
        this.setId(id);
    }
}
