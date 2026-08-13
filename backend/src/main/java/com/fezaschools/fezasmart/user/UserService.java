package com.fezaschools.fezasmart.user;

import com.fezaschools.fezasmart.events.BeforeDeleteRole;
import com.fezaschools.fezasmart.events.BeforeDeleteUser;
import com.fezaschools.fezasmart.role.Role;
import com.fezaschools.fezasmart.role.RoleRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import java.util.HashSet;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ApplicationEventPublisher publisher;

    public UserService(final UserRepository userRepository, final RoleRepository roleRepository,
            final ApplicationEventPublisher publisher) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.publisher = publisher;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Integer id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Integer id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Integer id) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteUser(id));
        userRepository.delete(user);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setHashedPassword(user.getHashedPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setIsActive(user.getIsActive());
        userDTO.setEmailVerified(user.getEmailVerified());
        userDTO.setPhoneVerified(user.getPhoneVerified());
        userDTO.setTwoFactorEnabled(user.getTwoFactorEnabled());
        userDTO.setTwoFactorMethod(user.getTwoFactorMethod());
        userDTO.setTwoFactorSecret(user.getTwoFactorSecret());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setLastLoginAt(user.getLastLoginAt());
        userDTO.setDeletedAt(user.getDeletedAt());
        userDTO.setDeletedBy(user.getDeletedBy());
        userDTO.setRestoreToken(user.getRestoreToken());
        userDTO.setUserRoleRoles(user.getUserRoleRoles().stream()
                .map(role -> role.getId())
                .toList());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setHashedPassword(userDTO.getHashedPassword());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setIsActive(userDTO.getIsActive());
        user.setEmailVerified(userDTO.getEmailVerified());
        user.setPhoneVerified(userDTO.getPhoneVerified());
        user.setTwoFactorEnabled(userDTO.getTwoFactorEnabled());
        user.setTwoFactorMethod(userDTO.getTwoFactorMethod());
        user.setTwoFactorSecret(userDTO.getTwoFactorSecret());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setUpdatedAt(userDTO.getUpdatedAt());
        user.setLastLoginAt(userDTO.getLastLoginAt());
        user.setDeletedAt(userDTO.getDeletedAt());
        user.setDeletedBy(userDTO.getDeletedBy());
        user.setRestoreToken(userDTO.getRestoreToken());
        final List<Role> userRoleRoles = roleRepository.findAllById(
                userDTO.getUserRoleRoles() == null ? List.of() : userDTO.getUserRoleRoles());
        if (userRoleRoles.size() != (userDTO.getUserRoleRoles() == null ? 0 : userDTO.getUserRoleRoles().size())) {
            throw new NotFoundException("one of userRoleRoles not found");
        }
        user.setUserRoleRoles(new HashSet<>(userRoleRoles));
        return user;
    }

    @EventListener(BeforeDeleteRole.class)
    public void on(final BeforeDeleteRole event) {
        // remove many-to-many relations at owning side
        userRepository.findAllByUserRoleRolesId(event.getId()).forEach(user ->
                user.getUserRoleRoles().removeIf(role -> role.getId().equals(event.getId())));
    }

}
