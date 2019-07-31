package com.agencybanking.security.jwt.session;

import com.agencybanking.core.data.Data;
import com.agencybanking.security.users.User;
import io.jsonwebtoken.lang.Assert;

import javax.persistence.*;
import java.util.Date;

@lombok.Data
@Entity
@Table(name = "sec_user_session")
@SequenceGenerator(name = "defaultSequenceGen", initialValue = 1000, sequenceName = "SEC_USER_SESS_SEQ", allocationSize = 1)
public class Session extends Data {

//    @Lob
//    @Column(name = "token", nullable = false)
//    private String token;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "domain", nullable = true)
    private String domain;

    @Id
    @Column(length = 500)
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created;

    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_accessed")
    private Date lastAccess;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires", nullable = false)
    private Date expires;

    @Column(name = "ip")
    private String ip;

    public Session() {

    }

    public Session(User user, Long tokenCreation) {
        Assert.notNull(user, "cannot create a SessionEvent without a user");
        Assert.notNull(tokenCreation, "cannot create a SessionEvent without a token creation date");
        this.username = user.getUsername();
        this.user = user;
        this.lastAccess = new Date(tokenCreation);
//        this.token = token;
        this.created = new Date();
        this.id = Session.buildId(user.getUsername(), tokenCreation);
    }

    public static String buildId(String username, Long tokenCreation) {
        return username + "_" + tokenCreation;
    }

    public void resetLastAccess() {
        this.lastAccess = new Date();
    }
    
}
