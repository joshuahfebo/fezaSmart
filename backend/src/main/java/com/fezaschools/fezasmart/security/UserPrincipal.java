package com.fezaschools.fezasmart.security;

import com.fezaschools.fezasmart.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;


@Getter
public class UserPrincipal implements UserDetails {

    private final Integer id;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final Integer schoolId;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Integer id, String username, String password, boolean enabled,
            Integer schoolId, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.schoolId = schoolId;
        this.authorities = authorities;
    }

    public static UserPrincipal from(User user) {
        Collection<GrantedAuthority> authorities = user.getUserRoleRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        return new UserPrincipal(user.getId(), user.getUsername(), user.getHashedPassword(),
                Boolean.TRUE.equals(user.getIsActive()), null, authorities);
    }

    public static UserPrincipal from(User user, Integer schoolId) {
        Collection<GrantedAuthority> authorities = user.getUserRoleRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        return new UserPrincipal(user.getId(), user.getUsername(), user.getHashedPassword(),
                Boolean.TRUE.equals(user.getIsActive()), schoolId, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return enabled;
    }
}