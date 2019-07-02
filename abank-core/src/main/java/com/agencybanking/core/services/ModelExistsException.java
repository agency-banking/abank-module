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
public class ModelExistsException extends MbcpServiceException {
	public static final String ERR_COMP_CODE_EXISTS = "core.company.codeexists";
    public static final String ERR_TEMPLATE_NAME_EXISTS = "messaging.template.exists";
    //Could not generate company code. Number of retries exceeded!
	private String message;
	private Object[] params;

	public ModelExistsException() {

	}

	public ModelExistsException(String message) {
		super(message);
	}

	public ModelExistsException(String message, Object...params) {
		super(message);
		this.params = params;
		this.message = message;
	}
	
	
}
