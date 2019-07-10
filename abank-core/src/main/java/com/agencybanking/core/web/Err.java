/**
 * 
 */
package com.agencybanking.core.web;

/**
 * @author dubic
 *
 */
public class Err {
	private String code, message;

	public Err() {
	}

	public Err(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Err [code=" + code + ", message=" + message + "]";
	}
}
