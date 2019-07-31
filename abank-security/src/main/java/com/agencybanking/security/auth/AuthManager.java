package com.agencybanking.security.auth;

import com.agencybanking.security.SecurityModule;
import com.agencybanking.security.auth.data.AuthCredentials;
import com.agencybanking.security.auth.data.AuthPrincipal;
import com.agencybanking.security.auth.data.OptimusAuthentication;
import com.agencybanking.security.password.PasswordService;
import com.agencybanking.security.users.User;
import com.agencybanking.security.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author dubic
 */
@Slf4j
@Component
public class AuthManager implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;



    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        log.debug("Authenticated ? {}",a.isAuthenticated());
        if (a.isAuthenticated()){
            log.debug("Already authenticated. return same");
            return a;
        }
        AuthPrincipal principal = (AuthPrincipal) a.getPrincipal();
        AuthCredentials credentials = (AuthCredentials) a.getCredentials();

        log.info("Username found : " + principal.getLoginId().trim());
        User user = userService.loadUserByUsername(principal.getLoginId().trim());//username = 'root@domain'
        // validations
        if (user == null) {
            throw new BadCredentialsException(SecurityModule.Companion.getERR_NO_ACCOUNT());
        }

		if (userService.isAccountLocked(user)) {
			throw new LockedException(SecurityModule.Companion.getERR_ACCOUNT_LOCKED());
		}
        // check password
        checkPassword(credentials.getPassword(), user);

        passwordExpired(user);
        // post authentication checks
        isUserActive(user);

        UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(user, "");
        return userAuth;
    }

    private void passwordExpired(User user) {

    }

    private void isUserActive(User user) {
        if (!user.getActive()) {
            throw new DisabledException(SecurityModule.Companion.getERR_USER_INACTIVE());
        }
    }

    private void checkPassword(String credential, User user) {
        boolean passwordMatched = passwordService.matchPassword(credential, user.getPassword());

//		SecurityPolicy policy = securityService.getSecurityPolicy(principal.getApplication());
//		UserSettings userSettings = userService.loadUserSettings(user, principal.getApplication(), policy);
        if (!passwordMatched) {
//			handleFailedLogin(user, principal.getApplication(), userSettings, policy);
            throw new BadCredentialsException(SecurityModule.Companion.getERR_BAD_CREDENTIALS());
        }
    }

//	public void handleFailedLogin(User user, String application, UserSettings userSettings, SecurityPolicy policy) {
//		int count = userSettings.incrementLoginCount();
//
//		if (count >= policy.getMaxLoginAttempts()) {
//			userService.lockUser(user, application, "Maximum login attempts exceeded " + policy.getMaxLoginAttempts(),
//					policy.getLockTimeout());
//			throw new LockedException("sec.authentication.account.locked");
//		}
//	}

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(OptimusAuthentication.class);
    }

}
