/**
 * 
 */
package com.agencybanking.security.auth.data;


import com.agencybanking.core.data.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author dubic
 *
 */
@lombok.Data
public class AuthenticationRequest extends Data implements Serializable {

	private static final long serialVersionUID = -8445943548965154778L;
	@NotNull(message = "security.principal.required")@Valid private AuthPrincipal principal;
	@NotNull(message = "security.credentials.required")@Valid private AuthCredentials credentials;

}
