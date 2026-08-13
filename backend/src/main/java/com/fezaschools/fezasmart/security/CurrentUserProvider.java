package com.fezaschools.fezasmart.security;

import com.fezaschools.fezasmart.user.User;
import com.fezaschools.fezasmart.user.UserRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class CurrentUserProvider {

    private final UserRepository userRepository;

    public CurrentUserProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<String> getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof String username) {
            return Optional.of(username);
        }
        if (principal instanceof UserPrincipal userPrincipal) {
            return Optional.of(userPrincipal.getUsername());
        }
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            return Optional.of(userDetails.getUsername());
        }
        return Optional.empty();
    }

    public User requireCurrentUser() {
        String username = getCurrentUsername()
                .orElseThrow(() -> new NotFoundException("No authenticated user"));
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Integer getCurrentUserId() {
        return requireCurrentUser().getId();
    }
}