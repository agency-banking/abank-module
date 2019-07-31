package com.agencybanking.security.auth;

import com.agencybanking.security.auth.data.AuthenticationRequest;
import com.agencybanking.security.auth.data.OptimusAuthentication;

public class SecurityHolder {
    private ThreadLocal<AuthenticationRequest> authRequestThreadLocal = new ThreadLocal<>();
    private ThreadLocal<OptimusAuthentication> authThreadLocal = new ThreadLocal<>();
    private static SecurityHolder myObj;

    private SecurityHolder() {
    }

    public static SecurityHolder getInstance() {
        if (myObj == null) {
            myObj = new SecurityHolder();
        }
        return myObj;
    }


    public void setAuthRequest(AuthenticationRequest authRequest) {
        authRequestThreadLocal.set(authRequest);
    }

    public AuthenticationRequest getAuthRequest() {
        return authRequestThreadLocal.get();
    }

    public void setAuthentication(OptimusAuthentication auth) {
        authThreadLocal.set(auth);
    }

    public OptimusAuthentication getAuthentication() {
        return authThreadLocal.get();
    }
}
