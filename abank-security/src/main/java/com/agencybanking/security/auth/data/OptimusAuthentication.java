package com.agencybanking.security.auth.data;

import com.agencybanking.security.users.User;
import lombok.Data;
import org.dom4j.Branch;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class OptimusAuthentication implements Authentication {
    private User user;
    private String token;
    private boolean authenticated;
    private Collection<GrantedAuthority> authorities;
//    private Company company;
    private Branch branch;
    private Object details;
    private String roles;

    public OptimusAuthentication(Collection<? extends GrantedAuthority> authorities) {
//        super(authorities);

    }

    public OptimusAuthentication() {
//        super(new HashSet<>());
    }


    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        this.authenticated = b;
    }

    @Override
    public String getName() {
        return null;
    }


    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

}
