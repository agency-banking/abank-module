package com.agencybanking.security.tokens;

import com.agencybanking.core.services.BizServiceException;
import com.agencybanking.core.utils.Utils;
import com.agencybanking.security.SecurityModule;
import com.agencybanking.security.auth.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author dubic
 */
@Service
@Slf4j
public class TokenService {

    private TokenRepository tokenRepository;
    private final SecurityModule securityModule;

    public TokenService(TokenRepository tokenRepository, SecurityModule securityModule) {
        this.tokenRepository = tokenRepository;
        this.securityModule = securityModule;
    }


    public Token create(Token token) {
        Token created = this.tokenRepository.save(token);
        log.debug("{} Token created : {}", token.getTokenType(), token.getToken());
        return created;
    }


    public Token update(Token token) {
        Token found = this.tokenRepository.findById(token.getId()).orElseThrow(() -> new IllegalStateException("Resource not found"));
        found.copyForUpdate(token);
        Token saved = this.tokenRepository.save(found);
        return saved;
    }

    public Token createOrUpdate(Token token) {
        List<Token> tokenList = tokenRepository.findByDataRefAndTokenType(token.getDataRef(), token.getTokenType());
        if (!tokenList.isEmpty()) {
            tokenRepository.deleteAll(tokenList);
        }
        Token created = this.tokenRepository.save(token);
        log.debug("{} Token created : {}", token.getTokenType(), token.getToken());
        return created;
    }


    public void delete(Token token) {
        tokenRepository.delete(token);
    }


    public Optional<Token> findById(Long id) {
        return tokenRepository.findById(id);
    }

    public Page<Token> query(Token token, PageRequest p) {
        if (ObjectUtils.isEmpty(token)) {
            return tokenRepository.findAll(p);
        }
        return tokenRepository.findAll(Example.of(token), p);
    }

    public boolean exists(Token token, Long id) {
        return tokenRepository.exists(Example.of(token));
    }

    public Optional<Token> getUnusedToken(String tok, String dataRef) {
        return this.tokenRepository.findByTokenAndDataRefAndUsedIsFalse(tok, dataRef).stream().findFirst();
    }

    public boolean isValidToken(Token token) {
        //check expired
        return token.isExpired();
    }

    public void invalidateToken(Token token) {
        token.invalidate();
        this.tokenRepository.save(token);
    }

    public Token buildToken(TokenType tokenType, String dataRef) {
        Token built = Token.builder().createDate(new Date())
                .dataRef(dataRef)
                .tokenType(tokenType)
                .used(false)
                .build();
        switch (tokenType) {
            case PASSWORD_RESET_LINK:
                int linkExpiry = securityModule.getPasswordPolicy().getResetLinkExpirationHrs();
                built.setExpiryDate(LocalDateTime.now().plusHours(linkExpiry));
                built.setToken(UUID.randomUUID().toString());
                break;
            case PASSWORD_RESET_TOKEN:
                int minsExpiry = securityModule.getPasswordPolicy().getResetTokenExpirationHrs();
                built.setExpiryDate(LocalDateTime.now().plusMinutes(new Long(minsExpiry)));
                built.setToken(org.apache.commons.lang3.StringUtils.leftPad(new Random().nextInt(1000000) + "", 6, "0"));
                break;
            case SOFT_TOKEN:
                built.setExpiryDate(LocalDateTime.now().plusMinutes(10));
                built.setToken(Utils.generateSoftToken(6));
                break;
            case FIRST_LOGIN:
                minsExpiry = securityModule.getPasswordPolicy().getResetTokenExpirationHrs();
                built.setExpiryDate(LocalDateTime.now().plusMinutes(10));
                built.setToken(org.apache.commons.lang3.StringUtils.leftPad(new Random().nextInt(1000000) + "", 6, "0"));
                break;
        }
        return create(built);
    }

    public Token buildSoftToken(TokenType tokenType, String dataRef) {
        Token built = Token.builder().createDate(new Date())
                .dataRef(dataRef)
                .tokenType(tokenType)
                .used(false)
                .build();
        if (TokenType.SOFT_TOKEN.equals(tokenType)) {
            built.setExpiryDate(LocalDateTime.now().plusMinutes(10));
            built.setToken(Utils.generateSoftToken(6));
        }
        return createOrUpdate(built);
    }

    public void verifyToken(String userToken) {
        Token token = this.getUnusedToken(userToken, AuthenticationService.currentAuditDetails().getCreatedBy())
                .orElseThrow(() -> new BizServiceException(SecurityModule.Companion.getERROR_INVALID_TOKEN()));
        log.debug("Token found {}", token);
        if (token.isExpired()) {
            throw new BizServiceException(SecurityModule.Companion.getERROR_EXPIRED_TOKEN());
        }
        this.invalidateToken(token);
    }
}
