package com.agencybanking.core.utils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter extends XmlAdapter<Date, Date> {
    DateFormat f = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");

    @Override
    public Date unmarshal(Date v) throws Exception {
        String format = f.format(v);
        // System.out.println("Parsed date: " + f.parse(format));
        return f.parse(format);
    }

    @Override
    public Date marshal(Date v) throws Exception {
        return v;
    }
}