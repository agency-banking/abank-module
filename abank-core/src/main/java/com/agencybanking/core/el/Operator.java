package com.agencybanking.core.el;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Operator extends com.agencybanking.core.data.Data {
    private String symbol;
    private String name;
    private boolean function = false;//should wrap the value in a function ()

    public Operator(String symbol, String name) {
        super();
        this.symbol = symbol;this.name=name;
    }
}
