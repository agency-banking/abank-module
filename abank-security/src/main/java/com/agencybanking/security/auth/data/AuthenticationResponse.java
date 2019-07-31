/**
 * 
 */
package com.agencybanking.security.auth.data;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dubic
 *
 */
@Data
@Builder
public class AuthenticationResponse extends com.agencybanking.core.data.Data implements Serializable {
	private static final long serialVersionUID = 1250166508152483573L;
	private String token;
	private String code;
	private OptimusAuthentication auth;
	private String authorities;
//	private String username;

}
