package com.agencybanking.security.auth.settings;

import com.agencybanking.core.data.BaseEnum;
import com.agencybanking.core.web.lists.DataList;

@DataList
public enum ProviderType implements BaseEnum {
    LDAP("LDAP"),
    TOKEN_SERVER("Token Server"),
    SOFT_TOKEN("Soft Token");

    private String description;

    ProviderType(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name();
    }
}
