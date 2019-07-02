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
public class MbcpServiceException extends RuntimeException {
    public static final String ERR_COMP_EXISTS = "core.company.exists";

    protected String message;
    protected Object[] params;

    public MbcpServiceException() {

    }

    public MbcpServiceException(String message) {
        super(message);
        this.message = message;
    }

    public MbcpServiceException(String message, Object... params) {
        super(message);
        this.params = params;
        this.message = message;
    }

    public MbcpServiceException(Exception e) {
        super(e);
    }
}
