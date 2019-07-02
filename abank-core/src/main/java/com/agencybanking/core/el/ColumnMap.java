package com.agencybanking.core.el;

import com.agencybanking.core.data.Data;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import java.lang.reflect.Field;

@lombok.Data
public class ColumnMap extends Data {
    private String label;
    private String column;
    private String ccyColumn;
    private String type;

    private static String buildColumn(Field f) {
        Column col = f.getAnnotation(Column.class);
        if (col != null) {
            String dbName = col.name();
            if (!ObjectUtils.isEmpty(dbName)) {
                return dbName.toLowerCase();
            }
        }
        //get column from field name
        StringBuffer buffer = new StringBuffer();
        for (char c : f.getName().toCharArray()) {
            if (Character.isUpperCase(c)) {
                buffer.append("_");
            }
            buffer.append(c);
        }
        return buffer.toString().toLowerCase();
    }

    public static ColumnMap simpleMap(Field f, Label label) {
        String col = buildColumn(f);
        if (StringUtils.isEmpty(col)) {
            return null;
        }
        ColumnMap map = new ColumnMap();
        map.setType(f.getType().getSimpleName());
        map.setLabel(label.value());
        map.setColumn(col);
        return map;
    }

    public static ColumnMap attributesColumnMap(Field parentField, Label label, Field f) {
        ColumnMap map = new ColumnMap();
        map.setType(f.getType().getSimpleName());
        Label parentLabel = parentField.getAnnotation(Label.class);
        map.setLabel(parentLabel.value() + " " + label.value());
        map.setColumn(getColumnName(f, parentField));
        return map;
    }

    private static String getColumnName(Field f, Field parentField) {
        AttributeOverrides attributeOverrides = parentField.getAnnotation(AttributeOverrides.class);
        if (attributeOverrides != null) {
            AttributeOverride[] columns = attributeOverrides.value();
            for (AttributeOverride col : columns) {
                if (f.getName().equalsIgnoreCase(col.name())) {
                    return col.column().name().toLowerCase();
                }
            }
        }
        return null;
    }
}
