package com.agencybanking.core.utils;

import com.agencybanking.core.data.NameValue;
import org.springframework.util.ObjectUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class NameValueAdapter extends XmlAdapter<String, NameValue> {
    @Override
    public NameValue unmarshal(String v) throws Exception {
//        System.out.println("Name Value Adapter::::: " + v);
        if (ObjectUtils.isEmpty(v)) {
            return null;
        } else {
            return new NameValue(v, "");
        }
    }

    @Override
    public String marshal(NameValue v) throws Exception {
        return v.getId();
    }
}
