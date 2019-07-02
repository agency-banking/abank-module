package com.agencybanking.core.el;

import com.agencybanking.core.utils.Utils;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class ClassMap extends com.agencybanking.core.data.Data {
    private String label;
    private String fullName;
    private String varName;

    public ClassMap() {

    }

    public ClassMap(String cn, Label label) {
        this();
        this.label = label.value();
        this.fullName = cn;
        this.varName = StringUtils.isEmpty(label.variable()) ? Utils.simpleName(cn, true) : label.variable();
    }
}
