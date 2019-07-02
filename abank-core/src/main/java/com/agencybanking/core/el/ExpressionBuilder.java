package com.agencybanking.core.el;

import com.agencybanking.core.data.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.*;

@Service
@Slf4j
public class ExpressionBuilder {
    //company.tin
    public List<ClassMap> mapTypes(List<String> classNames) {
        List<ClassMap> maps = new ArrayList<>();
        for (String cn : classNames) {
            Class<?> cls = null;
            try {
                cls = Class.forName(cn);
                Label label = cls.getAnnotation(Label.class);
                if (label == null) {
                    continue;
                }
                ClassMap map = new ClassMap(cn, label);
                maps.add(map);

            } catch (ClassNotFoundException | IllegalStateException e) {
                e.printStackTrace();
            }
        }

        return maps;
    }

    /**
     * list all the fields in the class with @Label annotation
     * uses <code>Class.forName</code> recipients load class definitions
     *
     * @param className the full class name
     * @return
     */
    public List<FieldMap> mapFields(String className) {
        List<FieldMap> maps = new ArrayList<>();
        try {
            Class<?> cls = Class.forName(className);
            addFieldsWithLabel(cls, maps, null, "", null);
            //extension
            Label label = cls.getAnnotation(Label.class);
            if (label != null) {
                Class<?> extension = label.extension();
                if (extension != Object.class) {
                    log.debug("Non Object.class extension detected : {}", extension.getName());
                    addFieldsWithLabel(extension, maps, null, "", null);
                }
            }

            if (BaseEntity.class.isAssignableFrom(cls)) {
                addFieldsWithLabel(BaseEntity.class, maps, null, "", null);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return maps;
    }

    private boolean addFieldsWithLabel(Class<?> cls, List<FieldMap> maps, String parent, String parentLabel, Label first) {
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field f : declaredFields) {
            Label label = f.getAnnotation(Label.class);
            if (label == null) {
                continue;
            }

            FieldMap map = new FieldMap(f, label, parent, parentLabel);
            if (!ObjectUtils.isEmpty(first) && !ObjectUtils.isEmpty(first.codeListValue()))
                map.setCodeList(first.codeListValue());
            if (!ObjectUtils.isEmpty(first) && !ObjectUtils.isEmpty(first.enumListValue()))
                map.setEnumList(first.enumListValue());


            if (label.embedded()) {
                log.info("Embedded label detected in :" + f.getName());
                addFieldsWithLabel(f.getType(), maps, f.getName(), label.value(), label);
            } else
                maps.add(map);
        }

        return false;
    }

    public List<ColumnMap> mapColumnFields(String className) {
        List<ColumnMap> maps = new ArrayList<>();
        try {
            Class<?> cls = Class.forName(className);
            addColumnFieldsWithLabel(cls, maps, null);
            //extension
            Label label = cls.getAnnotation(Label.class);
            if (label != null) {
                Class<?> extension = label.extension();
                if (extension != Object.class) {
                    log.debug("Non Object.class extension detected : {}", extension.getName());
                    addColumnFieldsWithLabel(extension, maps, null);
                }
            }

            if (BaseEntity.class.isAssignableFrom(cls)) {
                addColumnFieldsWithLabel(BaseEntity.class, maps, null);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return maps;
    }

    private boolean addColumnFieldsWithLabel(Class<?> cls, List<ColumnMap> maps, Field parentField) {
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field f : declaredFields) {
            Label label = f.getAnnotation(Label.class);
            if (label == null || f.getAnnotation(Transient.class) != null) {
                continue;
            }

//has parent field
            if (parentField != null) {
                maps.add(ColumnMap.attributesColumnMap(parentField, label, f));
                continue;
            }

            if (label.embedded()) {
                addColumnFieldsWithLabel(f.getType(), maps, f);
            } else {
                maps.add(ColumnMap.simpleMap(f, label));
            }
        }
        return false;
    }

    public List<Operator> operators() {
        return Arrays.asList(
                new Operator("=", "Equals"),
                new Operator("!=", "Not Equals"),
                new Operator(">", "Greater Than"),
                new Operator("<", "Less Than"),
                new Operator(">=", "Greater Than or Equals"),
                new Operator("<=", "Less Than or Equals"),
                new Operator("startsWith", "Begins With", true),
                new Operator("endsWith", "Ends With", true),
                new Operator("contains", "Contains", true)
        );
    }

    /**
     * Creates a map of variable name recipients instance value from a list of objects.
     * the map key is based on {@code Label} annotation's 'variable' value. if none is present or @Label is missing, the
     * Class's simple name with first character uncapitalized will be used
     *
     * @param values list of instance values or {@code Map<String,Object>}
     * @return the created map or empty map
     */
    public Map<String, Object> mapObjects(List values) {
        Map<String, Object> vars = new HashMap<>();
        for (Object o : values) {
            if (o instanceof Map) {
                ((Map<String, Object>) o).forEach((key, value) -> {
                    vars.put(key, value);
                });
                continue;
            }
            Label label = o.getClass().getAnnotation(Label.class);
            if (label == null || ObjectUtils.isEmpty(label.variable())) {
                vars.put(StringUtils.uncapitalize(o.getClass().getSimpleName()), o);
                continue;
            }
            vars.put(label.variable(), o);
        }
        return vars;
    }
}
