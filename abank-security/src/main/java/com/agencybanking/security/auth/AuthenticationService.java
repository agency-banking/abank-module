/**
 *
 */
package com.agencybanking.security.auth;

import com.agencybanking.core.data.AuditDetails;
import com.agencybanking.core.services.BaseService;
import com.agencybanking.core.services.BizServiceException;
import com.agencybanking.core.validation.ValidationUtils;
import com.agencybanking.security.SecurityModule;
import com.agencybanking.security.auth.data.*;
import com.agencybanking.security.jwt.JwtSubject;
import com.agencybanking.security.jwt.JwtTokenUtil;
import com.agencybanking.security.jwt.session.SessionStore;
import com.agencybanking.security.tokens.TokenRepository;
import com.agencybanking.security.tokens.TokenType;
import com.agencybanking.security.users.User;
import com.agencybanking.security.users.UserPermission;
import com.agencybanking.security.users.UserPersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dubic
 */
@Slf4j
@Service
public class AuthenticationService extends BaseService {

    public static final String TOKEN_HEADER = "Authorization";

    public static final String KEY_AUTH_LEVEL = "authlvl";
    private static final String KEY_COMPANY_USER_ID = "cmpyusrid";
    @Autowired
    public SessionStore sessionStore;
    @Autowired
    private AuthManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private LoginHistoryRepo loginRepo;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private MessageSource messages;
    @Autowired
    private UserPersRepository usrPersRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
        ValidationUtils.validate(authRequest);
        SecurityHolder.getInstance().setAuthRequest(authRequest);
        AuthPrincipal principal = authRequest.getPrincipal();
        AuthCredentials credentials = authRequest.getCredentials();
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(principal, credentials));
//first time login
        User user = (User) authentication.getPrincipal();
        if (user.getFirstLogin()) {
            OptimusAuthentication auth = new OptimusAuthentication();
            auth.setUser(user);
            String token = jwtTokenUtil.generateFirstTimeToken(user.getUsername());
            AuthenticationResponse resp = AuthenticationResponse.builder().token(token).auth(auth).code(TokenType.FIRST_LOGIN.name()).build();
//            success(resp, SecurityModule.INFO_FIRST_TIME_LOGIN);
            return resp;
        }
        return buildAuthenticationResponse(user, 0);
    }

    /**
     * checks if another authentication is queued, then
     *
     * @return
     */
    private AuthenticationResponse buildAuthenticationResponse(User user, int currentLevel) {
        // should token be generated
//        Optional<AuthConfig> authType = nextAuthentication(user, currentLevel);
//        if (authType.isPresent()) {
            // get authentication provider
//            AuthProvider provider = getRequiredProvider(authType.get())
//                    .orElseThrow(() -> new ProviderNotFoundException(SecurityModule.ERR_PROVIDER_NOT_FOUND,
//                            authType.get().getType()));
//            provider.preAuthenticate(companyUser);
//            // create access token
//            Map<String, Object> idmap = new HashMap<>();
//            idmap.put(KEY_COMPANY_USER_ID, companyUser.getId());
//            idmap.put(KEY_AUTH_LEVEL, authType.get().getLevel());
//            String accessToken = jwtTokenUtil.generateAccessToken(idmap);
//
//            return AuthenticationResponse.builder().token(accessToken).code(authType.get().getType().name()).build();
//        }
        // post authentication actions
        return authenticationSuccess(user);
    }


    /**
     * Called when all authentication succeeds. creates @code OptimusAuthentication
     *
     * @param user
     */
    public AuthenticationResponse authenticationSuccess(User user) {
// generate token because all authentication succeeded
        OptimusAuthentication auth = new OptimusAuthentication();
        auth.setUser(user);
//        user.setPassword(null);

        AuthenticationResponse response = AuthenticationResponse.builder().build();
        JwtSubject subject = new JwtSubject(user);
        final String token = jwtTokenUtil.generateToken(subject);
        //session
        sessionStore.startSession(user, subject.getTokenCreation());
        //history
        loginRepo.save(LoginHistory.of(user));
        // create authentication for thread local
        recreateAuthentication(auth, token, getGrantedAuthorities(user.getUsername()));
        response.setToken(token);
        response.setAuth(auth);
        return response;
    }

    public Authentication recreateAuthentication(OptimusAuthentication auth, String token, Set<GrantedAuthority> galist) {
        auth.setAuthorities(galist);
        auth.setToken(token);
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return auth;
    }

    public Set<GrantedAuthority> getGrantedAuthorities(String username) {
        List<UserPermission> permissions = usrPersRepository.findByUsername(username);
        Set<GrantedAuthority> galist = new HashSet<>();
        // load authorities
        if (!ObjectUtils.isEmpty(permissions)) {
            for (UserPermission p : permissions) {
                galist.add(new SimpleGrantedAuthority(p.getAuthority()));
            }
        }
        return galist;
    }
    // private List<Authentication>

    public static AuditDetails currentAuditDetails() {

        Authentication possible = SecurityContextHolder.getContext().getAuthentication();
        if (possible != null && possible instanceof OptimusAuthentication) {
            OptimusAuthentication auth = (OptimusAuthentication) possible;
            User user = auth.getUser();
            AuditDetails audit = new AuditDetails(user.getUsername());
            return audit;
        }
        AuditDetails sysAudit = new AuditDetails("SYSTEM", "SYSTEM", "SYSTEM");
        return sysAudit;
    }

    /**
     * Checks if current user has this permission with the code
     *
     * @param code permission code e.g ROLE_*
     * @return true if the user has the permission
     */
    public static void hasPermission(String code) {
        Assert.hasLength(code, "permission code must be supplied");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().stream().filter(a -> a.getAuthority().equalsIgnoreCase(code)).findAny()
                .orElseThrow(() -> new BizServiceException(SecurityModule.Companion.getERR_ACCESS_DENIED(), code));
    }

    /**
     * Gets the list of permissions/authorities of user in session
     *
     * @return
     */
    public static List<String> authorities() {
        Authentication possible = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = possible.getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
//		if (possible != null && possible instanceof OptimusAuthentication) {
//			OptimusAuthentication auth = (OptimusAuthentication) possible;
//		}
    }

    private void checkUserSession(User user) {
        if (!user.getConcurrentFg()) {
            sessionStore.expungeSession(user.getUsername());
            return;
        }

    }
}
