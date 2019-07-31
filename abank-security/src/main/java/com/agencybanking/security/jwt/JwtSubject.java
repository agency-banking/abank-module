/**
 *
 */
package com.agencybanking.security.jwt;

import com.agencybanking.security.users.User;
import io.jsonwebtoken.lang.Assert;
import lombok.Data;

/**
 * @author dubic
 */
@Data
public class JwtSubject {
    private long tokenCreation;
    private String username;

    public JwtSubject(User user) {
        Assert.notNull(user, "cannot create a JwtSubject without a user");
        this.username = user.getUsername();
        this.tokenCreation = System.currentTimeMillis();
    }

    public JwtSubject(String username){
        Assert.notNull(username, "cannot create a JwtSubject without a username");
        this.username = username;
        this.tokenCreation = System.currentTimeMillis();
    }
}
