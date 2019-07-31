/**
 *
 */
package com.agencybanking.security.jwt;

import com.agencybanking.security.SecurityModule;
import com.agencybanking.security.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//01-{0}-1-12345

/**
 * @author dubic
 */
@Component
public class JwtTokenUtil {
    static final String CLAIM_KEY_SUB = "sub";
    static final String CLAIM_KEY_USERNAME = "username";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "created";
    static final String CLAIM_KEY_EXPIRED = "exp";
    static final String CLAIM_KEY_COMPANY = "iss";
    static final String CLAIM_KEY_GRANT = "grant";
    static final String CLAIM_KEY_ACCESS_MAP = "accessTokenDetail";

    @Autowired
    private SecurityModule securityModule;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private Long expiration;
//
//    @Value("${jwt.expiration.accesstoken}")
//    private Long accessTokenExpiry;
//
//    @Value("${jwt.expiration.firsttime}")
//    private Long firstTimeTokenExpiry;

    public JwtSubject getDetailsFromToken(String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return null;
            }
            String username = claims.getSubject();
            JwtSubject subject = new JwtSubject(username);
            subject.setTokenCreation((Long) claims.get(CLAIM_KEY_CREATED));
            return subject;
        } catch (Exception e) {
            logger.error("Method: getUsernameFromToken({})[{}]", token, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean validateToken(String authToken, User userDetails) {
        return true;
        // final String username = getUsernameFromToken(authToken);
        // final Date created = getCreatedDateFromToken(authToken);
        // final Date expiration = getExpirationDateFromToken(authToken);
        // return (username.equals(userDetails.getUsername()) &&
        // !isTokenExpired(authToken));
    }

    public boolean isTokenExpired(String token) {
        try {
            getClaimsFromToken(token);
            return false;
        } catch (ExpiredJwtException ex) {
            logger.warn("Token expired :{} :{}", token, ex.getMessage());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(securityModule.getJwt().getSecret()).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.error("getClaimsFromToken : " + e.getMessage());
//            e.printStackTrace();
            claims = null;
        }
        return claims;
    }

    public String generateToken(JwtSubject subject) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_SUB, subject.getUsername());
        claims.put(CLAIM_KEY_CREATED, subject.getTokenCreation());
        return doGenerateToken(claims, null);
    }

    public String generateAccessToken(Map<String, Object> tokenMap) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_ACCESS_MAP, tokenMap);
        return doGenerateToken(claims, securityModule.getJwt().getAccessTokenExpiry());
    }

    public String generateFirstTimeToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_SUB, username);
        final Date createdDate = new Date();
        claims.put(CLAIM_KEY_CREATED, createdDate);
        return doGenerateToken(claims, securityModule.getJwt().getFirstTimeTokenExpiry());
    }

    private String doGenerateToken(Map<String, Object> claims, @Nullable Long seconds) {
        if (seconds != null) {
            final Date createdDate = (Date) claims.get(CLAIM_KEY_CREATED);
            final Date expirationDate = new Date(createdDate.getTime() + seconds * 1000);
            return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, securityModule.getJwt().getSecret())
                    .compact();
        }
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, securityModule.getJwt().getSecret())
                .compact();
    }

    public String detailsFirstTimeToken(String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    public Map<String, Object> accessTokenDetails(String accessToken) {
        final Claims claims = getClaimsFromToken(accessToken);
        Map<String, Object> tokenMap = (Map<String, Object>) claims.get(CLAIM_KEY_ACCESS_MAP);
        return tokenMap;
    }
}
