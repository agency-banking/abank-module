package com.agencybanking.core.utils;

import com.agencybanking.core.data.Money;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MoneyAdapter extends XmlAdapter<String, Money> {
    @Override
    public Money unmarshal(String v) throws Exception {
        return new Money(v);
    }

    @Override
    public String marshal(Money v) throws Exception {
        return v.getValue();
    }
}

