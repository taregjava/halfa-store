package com.halfacode.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserPrincipalAuthenticationToken extends AbstractAuthenticationToken {

    private final UserPrincipal principal;

    public UserPrincipalAuthenticationToken(UserPrincipal principal) {
        super(principal.getAuthorities());
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal; // Return the UserPrincipal object
    }
}