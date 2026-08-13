package com.fezaschools.fezasmart.auth;

import com.fezaschools.fezasmart.auth.dto.AuthResponse;
import com.fezaschools.fezasmart.auth.dto.LoginRequest;
import com.fezaschools.fezasmart.auth.dto.RefreshTokenRequest;
import com.fezaschools.fezasmart.auth.dto.RegisterRequest;
import com.fezaschools.fezasmart.role.Role;
import com.fezaschools.fezasmart.role.RoleRepository;
import com.fezaschools.fezasmart.security.JwtTokenProvider;
import com.fezaschools.fezasmart.security.UserPrincipal;
import com.fezaschools.fezasmart.user.User;
import com.fezaschools.fezasmart.user.UserRepository;
import com.fezaschools.fezasmart.util.BusinessException;
import com.fezaschools.fezasmart.util.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.generateAccessToken(principal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(principal);

        return new AuthResponse(accessToken, refreshToken, "Bearer",
                principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                principal.getId());
    }

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsernameIgnoreCase(registerRequest.getUsername())) {
            throw new BusinessException("Username already taken");
        }
        if (registerRequest.getEmail() != null && !registerRequest.getEmail().isBlank()
                && userHasEmail(registerRequest.getEmail())) {
            throw new BusinessException("Email already registered");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setHashedPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setIsActive(true);
        user.setEmailVerified(false);
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());

        Set<Role> roles = resolveRoles(registerRequest.getRoles());
        user.setUserRoleRoles(roles);

        // Enforce creation restriction: only SUPER_ADMIN can create new admins.
        User saved = userRepository.save(user);
        UserPrincipal principal = UserPrincipal.from(saved);
        return new AuthResponse(jwtTokenProvider.generateAccessToken(principal),
                jwtTokenProvider.generateRefreshToken(principal), "Bearer",
                principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                saved.getId());
    }

    public AuthResponse refresh(RefreshTokenRequest refreshTokenRequest) {
        String token = refreshTokenRequest.getRefreshToken();
        if (!jwtTokenProvider.isValid(token)) {
            throw new BusinessException("Invalid or expired refresh token");
        }
        String username = jwtTokenProvider.extractUsername(token);
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserPrincipal principal = UserPrincipal.from(user);
        String accessToken = jwtTokenProvider.generateAccessToken(principal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(principal);
        return new AuthResponse(accessToken, refreshToken, "Bearer",
                principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                principal.getId());
    }

    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!passwordEncoder.matches(currentPassword, user.getHashedPassword())) {
            throw new BusinessException("Current password is incorrect");
        }
        user.setHashedPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(OffsetDateTime.now());
        userRepository.save(user);
    }

    private boolean userHasEmail(String email) {
        List<User> users = userRepository.findAll();
        return users.stream().anyMatch(u -> email.equalsIgnoreCase(u.getEmail()));
    }

    private Set<Role> resolveRoles(List<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        if (roleNames == null || roleNames.isEmpty()) {
            roles.add(requireRole("PARENT"));
            return roles;
        }
        for (String roleName : roleNames) {
            roles.add(requireRole(roleName));
        }
        return roles;
    }

    private Role requireRole(String name) {
        return roleRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("Role not found: " + name));
    }
}