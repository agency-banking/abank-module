package com.agencybanking.security.password;

import lombok.Data;

@Data
public class PasswordUpdate extends com.agencybanking.core.data.Data {

    private String oldPassword;
    private String password;
    private String passwordConfirm;

    public void clear() {
        this.oldPassword = null;
        this.password = null;
        this.passwordConfirm = null;
    }

}
