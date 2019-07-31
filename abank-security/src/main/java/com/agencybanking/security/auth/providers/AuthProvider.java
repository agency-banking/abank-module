package com.agencybanking.security.auth.providers;

import org.springframework.security.core.Authentication;

public interface AuthProvider {

    public Authentication authenticate();

    public String getCode();

}
