
package com.innovaturelabs.training.employee.management.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AccessTokenUserDetails implements UserDetails {

    // private static final List<GrantedAuthority> ROLES =
    // AuthorityUtils.createAuthorityList("ROLE_USER");

    private static final List<GrantedAuthority> ROLES = new ArrayList<>();

    public final String userRole;
    public final int userId;

    public AccessTokenUserDetails(int userId, byte role) {
        this.userId = userId;

        switch (role) {
            case 2:
                userRole = "ADMIN";
                break;
            case 1:
                userRole = "EMPLOYER";
                break;
            case 0:
                userRole = "EMPLOYEE";
                break;
            default:
                userRole = null;
                break;
        }

        ROLES.add(new SimpleGrantedAuthority("ROLE_" + userRole));

    }

    // public AccessTokenUserDetails(int userId) {
    // this.userId = userId;
    // }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ROLES;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
