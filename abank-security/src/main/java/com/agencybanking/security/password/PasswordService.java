package com.agencybanking.security.password;

import com.agencybanking.core.services.BaseService;
import com.agencybanking.core.services.BizServiceException;
import com.agencybanking.security.SecurityModule;
import com.agencybanking.security.auth.AuthenticationService;
import com.agencybanking.security.jwt.JwtTokenUtil;
import com.agencybanking.security.tokens.Token;
import com.agencybanking.security.tokens.TokenService;
import com.agencybanking.security.tokens.TokenType;
import com.agencybanking.security.users.User;
import com.agencybanking.security.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Random;

@Slf4j
@Service
public class PasswordService extends BaseService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private SecurityModule securityModule;

    public PasswordReset initForgotPassword(String username) throws Exception {
        Assert.hasLength(username, "Username required for reset ");
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new BizServiceException(SecurityModule.Companion.getERR_PWORD_RESET_NO_USER(), username);
        }
        //check send type
        String sendType = securityModule.getPasswordPolicy().getResetSendType();
//        String emailTpl = securityModule.getPasswordPolicy().getresetTokenTpl();
        TokenType tokenType = TokenType.PASSWORD_RESET_TOKEN;

//        if (PasswordReset.SEND_TYPE_LINK.equals(sendType)) {
//            emailTpl = securityService.getSecurityPolicy().getData().getPasswordPolicy().getPasswordresetLinkTpl();
//            tokenType = TokenType.PASSWORD_RESET_LINK;
//        }
        //build token
        Token token = tokenService.buildToken(tokenType, username);
        //send
//        appFlowTrigger.sendMessage("reset_password" + username, emailTpl,
//                MessageType.EMAIL, Arrays.asList(user.getEmail()), Arrays.asList(user, token));
        //audit
//        auditService.log(username, OptimusEvent.of(user).evt(EventType.PASSWORD_RESET).tenant(Company.DEFAULT));
        PasswordReset reset = new PasswordReset();
        reset.setUsername(username);
        if (PasswordReset.SEND_TYPE_LINK.equals(sendType)) {
            reset.setTokenType(TokenType.PASSWORD_RESET_LINK);
//            success(reset, SecurityModule.Companion.getSUCCESS_RESET_PASSWORD_LINK_EMAIL());
        } else {
            reset.setTokenType(TokenType.PASSWORD_RESET_TOKEN);
//            success(reset, SecurityModule.Companion.getSUCCESS_RESET_PASSWORD_TOKEN_EMAIL());
        }
        return reset;
    }

    public PasswordReset forgotPasswordReset(PasswordReset reset) {
        Assert.hasLength(reset.getToken(), "Token string not supplied");
        Token token = tokenService.getUnusedToken(reset.getToken(), reset.getUsername())
                .orElseThrow(() -> new BizServiceException(SecurityModule.Companion.getERROR_INVALID_TOKEN()));
        log.debug("Token found {}", token);
        if (token.isExpired()) {
            throw new BizServiceException(SecurityModule.Companion.getERROR_EXPIRED_TOKEN());
        }
        resetPassword(reset);
//        success(reset, SecurityModule.Companion.getSUCCESS_RESET_PASSWORD());
        reset.clear();
        //invalidate token
        tokenService.invalidateToken(token);
        return reset;
    }

    public PasswordReset firstLoginPasswordReset(PasswordReset reset) {
        Assert.hasLength(reset.getToken(), "Token string not supplied");
        if (jwtTokenUtil.isTokenExpired(reset.getToken())) {
            throw new BizServiceException(SecurityModule.Companion.getERROR_EXPIRED_TOKEN());
        }
        String userName = jwtTokenUtil.detailsFirstTimeToken(reset.getToken());
        if (StringUtils.isEmpty(userName)) {
            throw new BizServiceException(SecurityModule.Companion.getERROR_INVALID_TOKEN());
        }
        if (!userName.equals(reset.getUsername())) {
            log.error("Username supplied {} does not match token's {}", reset.getUsername(), userName);
            throw new BizServiceException(SecurityModule.Companion.getERROR_INVALID_TOKEN());
        }
        log.info("initiating first time login password reset...{}", userName);
        resetPassword(reset);
//        success(reset, SecurityModule.Companion.getSUCCESS_RESET_PASSWORD());
        reset.clear();
        return reset;
    }

    public void resetPassword(PasswordReset reset) {
        //validate passwords
        boolean equals = reset.getPassword().equals(reset.getPasswordConfirm());
        if (!equals) {
            throw new BizServiceException(SecurityModule.Companion.getERR_RESET_PASSWORD());
        }
        System.out.println("NEW PWORD::"+reset.getPassword());
        String encoded = encodePassword(reset.getPassword());
        System.out.println("NEW PWORD ENC::"+encoded);
        User user = userRepository.findByUsername(reset.getUsername());
        user.setPassword(encoded);
        user.setFirstLogin(false);
//        userRepository.updatePassword(encoded, reset.getUsername());
        userRepository.save(user);
        log.info("Password Reset {}", reset.getUsername());
//        auditService.log(reset.getUsername(), OptimusEvent.of(user).evt(EventType.PASSWORD_RESET).tenant(Company.DEFAULT));
    }

    public PasswordUpdate updatePassword(PasswordUpdate update) {
        boolean equals = update.getPassword().equals(update.getPasswordConfirm());
        if (!equals) {
            throw new BizServiceException(SecurityModule.Companion.getERR_RESET_PASSWORD());
        }
        User user = userRepository.findByUsername(AuthenticationService.currentAuditDetails().getCreatedBy());
        if (!passwordEncoder.matches(update.getOldPassword(), user.getPassword())) {
            throw new BizServiceException(securityModule.Companion.getERR_PASSWORD_UPDATE());
        }
        user.setPassword(encodePassword(update.getPassword()));
        log.info("Password Update {}", user.getUsername());
//        success(update, SecurityModule.SUCCESS_PASSWORD_UPDATED);
        update.clear();
        return update;
    }

    public String encodePassword(String password) {
        Assert.hasLength(password, "cannot encode null or empty password");
        return passwordEncoder.encode(password);
    }

    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String generatePassword() {
        String[] chars = "Q,W,E,R,T,Y,U,I,O,P,A,S,D,F,G,H,J,K,L,Z,X,C,V,B,N,M,0,1,2,3,4,5,6,7,8,9,0,q,w,e,r,t,y,u,i,o,p,a,s,d,f,g,h,j,k,l,z,x,c,v,b,n,m"
                .split(",");
        StringBuilder sb = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 8; i++) {
            int pos = random.nextInt(chars.length);
            sb.append(chars[pos]);
        }
        return sb.toString();
    }

}
