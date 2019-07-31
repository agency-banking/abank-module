package com.agencybanking.security.auth.providers;

import com.agencybanking.core.services.BizServiceException;

public class ProviderNotFoundException extends BizServiceException {

    protected String message;
    protected Object[] params;

    public ProviderNotFoundException() {

    }

    public ProviderNotFoundException(String message) {
        super(message);
    }

    public ProviderNotFoundException(String message, Object...params) {
        super(message);
        this.params = params;
        this.message = message;
    }
}
