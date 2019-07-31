package com.agencybanking.flow.bpmn.models;

import lombok.Data;

@Data
public class EndEvent extends BPMNNode{
    public EndEvent(){
    }

    public EndEvent(String id){
        this.setId(id);
    }
}
