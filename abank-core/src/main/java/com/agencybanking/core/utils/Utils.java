/**
 *
 */
package com.agencybanking.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author dubic
 */
public class Utils {

    public static boolean isEmpty(String s) {
        // return Optional.ofNullable(s).filter(t -> t == null).filter(t ->
        // t.isEmpty()).isPresent();
        if (s == null) {
            return true;
        }
        return s.trim().isEmpty();
    }

    public static String first(String s) {
        // return Optional.of(s).filter(t -> isEmpty(t)).
        if (s == null) {
            return null;
        }
        return s.trim();
    }

    public static String toJson(Object t) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJson(Object t, Class view, boolean pretty) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (pretty){
                return mapper.writerWithView(view).withDefaultPrettyPrinter().writeValueAsString(t);
            }
            return mapper.writerWithView(view).writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String s, TypeReference<T> ref) {
        ObjectMapper mapper = new ObjectMapper();
        try {

            return mapper.readValue(s, ref);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String s, Class<T> cls) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            TypeFactory typeFactory = mapper.getTypeFactory();
            JavaType javaType = typeFactory.constructType(GenericTypeResolver.resolveType(cls, (Class) null));
            return mapper.readValue(s, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String s, String clsName) throws ClassNotFoundException {
        Class<T> cls = (Class<T>) Class.forName(clsName);
        return fromJson(s, cls);
    }

    public static String[] fromCommaStringToArray(String s) {
        if (s == null) {
            return new String[]{};
        }
        return Optional.of(s).map(t -> t.split(",")).orElse(new String[]{});
    }

    /**
     * Safe substring without throwing <code>IndexOutOfBoundsException</code>. performs s.substring(0,chars)
     * if chars > s.length, then original string is returned.
     *
     * @param s     original string
     * @param chars index of end substring
     * @return
     */
    public static String first(String s, int chars) {
        try {
            if (s == null) {
                return s;
            }
            return s.substring(0, chars);
        } catch (IndexOutOfBoundsException e) {
            return s;
        }
    }

    /**
     * Get the last n chars of this string.
     * Safe substring without throwing <code>IndexOutOfBoundsException</code>. performs s.substring(s.length-chars)
     * if chars > s.length, then original string is returned.
     *
     * @param s     original string
     * @param chars index of end substring
     * @return
     */
    public static String last(int chars, String s) {
        int pos = s.length() - chars;
        if (pos < 0) {
            return s;
        }
        try {
            if (s == null) {
                return s;
            }
            return s.substring(pos);
        } catch (IndexOutOfBoundsException e) {
            return s;
        }
    }

    public static String hash(String principal) {
        return DigestUtils.md5DigestAsHex(principal.getBytes());
    }

    public static String nullSafeString(String str) {
        return StringUtils.isEmpty(str) ? "" : str.trim();
    }

    //for converting list of string recipients any messageType
    public static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
        return from.stream().map(func).collect(Collectors.toList());
    }

    //for converting array of string recipients any messageType
    public static <T, U> U[] convertArray(T[] from,
                                          Function<T, U> func,
                                          IntFunction<U[]> generator) {
        return Arrays.stream(from).map(func).toArray(generator);
    }

    public static Long[] convertStringIdsToLong(String ids, String delimiter) {
        if (StringUtils.isEmpty(delimiter)) {
            return convertStringIdsToLong(ids);
        }
        if (StringUtils.isEmpty(ids)) {
            return new Long[0];
        }
        String arrCommaSeparatedRoles[] = ids.split(delimiter);
        return convertArray(arrCommaSeparatedRoles, Long::parseLong, Long[]::new);
    }

    public static Long[] convertStringIdsToLong(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return new Long[0];
        }
        String arrCommaSeparatedRoles[] = ids.split(",");
        return convertArray(arrCommaSeparatedRoles, Long::parseLong, Long[]::new);
    }

    public static boolean matchAny(String value, String... patterns) {
        for (String regex : patterns) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(value);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNumber(String str) {
        return str.matches("\\d+");
    }

    /**
     * Get the simple name of a class from the full class name. if isBean is true, then the first letter is lowercased
     *
     * @param clazz
     * @param isBean
     * @return
     */
    public static String simpleName(String clazz, boolean isBean) {
        String simple = clazz.substring(clazz.lastIndexOf(".") + 1);
        if (isBean) {
            return StringUtils.uncapitalize(simple);
        }
        return simple;
    }

    public static List<Method> getAnnotatedMethods(Object bean, Class<? extends Annotation> annotationClass) {
        List<Method> annotatedMethods = new ArrayList<>();
        Class<?> objClz = bean.getClass();
        if (AopUtils.isAopProxy(bean)) {
            objClz = AopUtils.getTargetClass(bean);
        }
        for (Method method : objClz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    public static Object unmarshall(String xml, Class clazz) {
        StringReader sr = new StringReader(xml);
        return JAXB.unmarshal(sr, clazz);
    }

    public static Boolean getBoolean(Boolean value) {
        return ObjectUtils.isEmpty(value) ? Boolean.FALSE : value;
    }

    public static String generateSoftToken(int size) {
        return org.apache.commons.lang3.StringUtils.leftPad(new Random().nextInt(1000000) + "", size, "0");
    }

    @NotNull
    public static String leftPad(String str, String padStr, int size) {
        return org.apache.commons.lang3.StringUtils.leftPad(str, size, padStr);
    }
}
