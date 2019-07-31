package com.agencybanking.security.password;

import com.agencybanking.security.tokens.TokenType;
import lombok.Data;

@Data
public class PasswordReset extends com.agencybanking.core.data.Data {
    public static final String SEND_TYPE_TOKEN = "TOKEN";
    public static final String SEND_TYPE_LINK = "LINK";

    private String username;
    private String password;
    private String passwordConfirm;
    private String token;
    private TokenType tokenType;

    public void clear() {
        this.password = null;
        this.passwordConfirm = null;
    }
}
