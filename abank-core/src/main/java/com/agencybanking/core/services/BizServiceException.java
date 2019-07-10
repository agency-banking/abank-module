/**
 * 
 */
package com.agencybanking.core.services;

import lombok.Data;

/**
 * @author dubic
 *
 */
@Data
public class BizServiceException extends RuntimeException {
    public static final String ERR_COMP_EXISTS = "core.company.exists";

    protected String message;
    protected Object[] params;

    public BizServiceException() {

    }

    public BizServiceException(String message) {
        super(message);
        this.message = message;
    }

    public BizServiceException(String message, Object... params) {
        super(message);
        this.params = params;
        this.message = message;
    }

    public BizServiceException(Exception e) {
        super(e);
    }
}
