package com.halfacode.security;

import com.halfacode.entity.Role;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class UserPrincipal implements UserDetails {

    private final Long userId;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    // Implement the methods to retrieve user details and authorities based on your application logic
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        // Return the user's hashed password here
        return null;
    }

    @Override
    public String getUsername() {
        // Return the user's username here
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Return true if the user account is not expired
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Return true if the user account is not locked
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Return true if the user's credentials are not expired
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Return true if the user account is enabled
        return true;
    }
}