/**
 * 
 */
package com.agencybanking.core.validation;

import java.util.List;

/**
 * @author dubic
 *
 */
public class InvalidPropertyException extends RuntimeException {

	private List<String> errorMsgs;

	public InvalidPropertyException(List<String> errorMsgs) {
		super("Messgae here: "+ errorMsgs.toString());
		this.setErrorMsgs(errorMsgs);
	}

	public List<String> getErrorMsgs() {
		return errorMsgs;
	}

	public void setErrorMsgs(List<String> errorMsgs) {
		this.errorMsgs = errorMsgs;
	}

	
}
