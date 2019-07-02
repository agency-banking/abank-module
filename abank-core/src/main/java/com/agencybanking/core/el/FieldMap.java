package com.agencybanking.core.el;

import lombok.Data;

import java.lang.reflect.Field;

@Data
public class FieldMap extends com.agencybanking.core.data.Data {
    private String label;
    private String name;
    private String column;
    private String type;
    private String codeList;
    private String enumList;

    public FieldMap(Field f, Label label, String parent, String parentLabel) {
        super();
        if (parent != null) {
            this.label = parentLabel + " " + label.value();
            this.name = parent + "." + f.getName();
        } else {
            this.label = label.value();
            this.name = f.getName();
        }
        this.type = f.getType().getSimpleName();
        this.codeList = label.codeListValue();
        this.enumList = label.enumListValue();
    }

    public FieldMap() {

    }



//    public static FieldMap columnFields(Field f, Label label) {
//        String col = buildColumn(f);
//        if (StringUtils.isEmpty(col)){
//            return null;
//        }
//        FieldMap map = new FieldMap();
//        map.setType(f.getType().getSimpleName());
//        map.setLabel(label.value());
//        map.setName(f.getName());
//        map.setColumn(col);
//        return map;
//    }
}
