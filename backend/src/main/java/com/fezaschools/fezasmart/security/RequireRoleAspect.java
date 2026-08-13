package com.fezaschools.fezasmart.security;

import com.fezaschools.fezasmart.util.BusinessException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class RequireRoleAspect {

    private static final Set<String> SUPER_ADMIN_ROLES = Set.of("SUPER_ADMIN");

    @Before("@annotation(requireRole)")
    public void checkRole(RequireRole requireRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("Authentication required");
        }

        Set<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.startsWith("ROLE_") ? auth.substring(5) : auth)
                .collect(Collectors.toSet());

        // SUPER_ADMIN bypasses all role checks
        if (authorities.stream().anyMatch(SUPER_ADMIN_ROLES::contains)) {
            return;
        }

        Set<String> required = Arrays.stream(requireRole.value())
                .map(String::toUpperCase)
                .collect(Collectors.toSet());

        if (required.stream().noneMatch(authorities::contains)) {
            throw new com.fezaschools.fezasmart.util.ForbiddenException(
                    "Requires role: " + required);
        }
    }

    @Before("@annotation(requirePermission)")
    public void checkPermission(RequirePermission requirePermission) {
        checkRoleOverloaded(requirePermission.value());
    }

    private void checkRoleOverloaded(String[] requiredRoles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("Authentication required");
        }

        Set<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.startsWith("ROLE_") ? auth.substring(5) : auth)
                .collect(Collectors.toSet());

        if (authorities.stream().anyMatch(SUPER_ADMIN_ROLES::contains)) {
            return;
        }

        Set<String> required = Arrays.stream(requiredRoles)
                .map(String::toUpperCase)
                .collect(Collectors.toSet());

        if (required.stream().noneMatch(authorities::contains)) {
            throw new com.fezaschools.fezasmart.util.ForbiddenException(
                    "Requires role: " + required);
        }
    }
}