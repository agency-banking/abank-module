/**
 * 
 */
package com.agencybanking.security.auth.data;

import com.agencybanking.core.data.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author dubic
 *
 */
@lombok.Data
public class AuthCredentials extends Data implements Serializable {

	private static final long serialVersionUID = -8445943548965154778L;
	@NotBlank(message="Password is required")
	private String password;
	private String token;

}
