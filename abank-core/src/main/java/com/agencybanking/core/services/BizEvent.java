package com.agencybanking.core.services;

import com.agencybanking.core.data.BaseEntity;
import com.agencybanking.core.data.Data;
import com.agencybanking.core.data.Money;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.util.Assert.hasLength;
import static org.springframework.util.Assert.notNull;

@lombok.Data
@AllArgsConstructor
public class BizEvent extends Data {
    List values = new ArrayList<>();
    protected String module;
    protected String product;
    protected String code;
    protected String action;
    protected String companyCode;
    private boolean fireAppflow = true;
    private boolean elasticate;
    protected Long id;
    private String bankCode;
    private Money amount;
    protected String accountNo;
    private String searchKey;
    private boolean pendable;
    private boolean mdPendable;
    private String transactionRef;
    private Data data;
    private String permission;

    public BizEvent() {
    }

    private BizEvent(Object... values) {
        this.values = Arrays.asList(values);
        Object obj = this.values.get(0);
        if (obj instanceof BaseEntity) {
            BaseEntity e = (BaseEntity) obj;
            this.product = e.product();
            this.module = e.module();
            this.ref(e.getId());
        }
    }

    public BizEvent value(Object v) {
        this.values.add(v);
        return this;
    }

    public static BizEvent of(Object... values) {
        Assert.notEmpty(values, "Must pass one or more objects to Biz event");
        return new BizEvent(values);
    }

    public BizEvent module(String module) {
        this.module = module;
        return this;
    }

    public BizEvent product(String productCode) {
        this.product = productCode;
        return this;
    }

    public BizEvent action(String action) {
        this.action = action;
        return this;
    }

    public BizEvent tenant(String tenant) {
        this.companyCode = tenant;
        return this;
    }

    public BizEvent ref(Long id) {
        this.id = id;
        return this;
    }

    public BizEvent bankCode(String bankCode) {
        this.bankCode = bankCode;
        return this;
    }

    public BizEvent amount(Money amount) {
        this.amount = amount;
        return this;
    }

    public BizEvent accountNo(String accountNo) {
        this.accountNo = accountNo;
        return this;
    }

    public BizEvent code(String code) {
        this.code = code;
        return this;
    }

    public BizEvent searchKey(String searchKey) {
        this.searchKey = searchKey;
        return this;
    }

    public BizEvent transactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
        return this;
    }

    public BizEvent permission(String permission) {
        this.permission = permission;
        return this;
    }

    public BizEvent pendable(boolean pendable) {
        this.pendable = pendable;
        return this;
    }

    public BizEvent mdPendable(boolean mdPendable) {
        this.mdPendable = mdPendable;
        return this;
    }

    public BizEvent elasticate(boolean isElasticable) {
        this.elasticate = isElasticable;
        return this;
    }

    @Override
    public void validate() throws ConstraintViolationException {
        hasLength(module, "Module must be present");
        hasLength(product, "Product must be present");
        notNull(action, "Product Action must be present");
    }

    public BizEvent fireApproval(boolean shouldFireApproval) {
        this.fireAppflow = shouldFireApproval;
        return this;
    }
}
