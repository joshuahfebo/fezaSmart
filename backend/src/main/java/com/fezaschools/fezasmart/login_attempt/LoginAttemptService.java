package com.fezaschools.fezasmart.login_attempt;

import com.fezaschools.fezasmart.events.BeforeDeleteUser;
import com.fezaschools.fezasmart.user.User;
import com.fezaschools.fezasmart.user.UserRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;
    private final UserRepository userRepository;

    public LoginAttemptService(final LoginAttemptRepository loginAttemptRepository,
            final UserRepository userRepository) {
        this.loginAttemptRepository = loginAttemptRepository;
        this.userRepository = userRepository;
    }

    public List<LoginAttemptDTO> findAll() {
        final List<LoginAttempt> loginAttempts = loginAttemptRepository.findAll(Sort.by("id"));
        return loginAttempts.stream()
                .map(loginAttempt -> mapToDTO(loginAttempt, new LoginAttemptDTO()))
                .toList();
    }

    public LoginAttemptDTO get(final Integer id) {
        return loginAttemptRepository.findById(id)
                .map(loginAttempt -> mapToDTO(loginAttempt, new LoginAttemptDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final LoginAttemptDTO loginAttemptDTO) {
        final LoginAttempt loginAttempt = new LoginAttempt();
        mapToEntity(loginAttemptDTO, loginAttempt);
        return loginAttemptRepository.save(loginAttempt).getId();
    }

    public void update(final Integer id, final LoginAttemptDTO loginAttemptDTO) {
        final LoginAttempt loginAttempt = loginAttemptRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(loginAttemptDTO, loginAttempt);
        loginAttemptRepository.save(loginAttempt);
    }

    public void delete(final Integer id) {
        final LoginAttempt loginAttempt = loginAttemptRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        loginAttemptRepository.delete(loginAttempt);
    }

    private LoginAttemptDTO mapToDTO(final LoginAttempt loginAttempt,
            final LoginAttemptDTO loginAttemptDTO) {
        loginAttemptDTO.setId(loginAttempt.getId());
        loginAttemptDTO.setIpAddress(loginAttempt.getIpAddress());
        loginAttemptDTO.setAttemptedAt(loginAttempt.getAttemptedAt());
        loginAttemptDTO.setSuccess(loginAttempt.getSuccess());
        loginAttemptDTO.setUser(loginAttempt.getUser() == null ? null : loginAttempt.getUser().getId());
        return loginAttemptDTO;
    }

    private LoginAttempt mapToEntity(final LoginAttemptDTO loginAttemptDTO,
            final LoginAttempt loginAttempt) {
        loginAttempt.setIpAddress(loginAttemptDTO.getIpAddress());
        loginAttempt.setAttemptedAt(loginAttemptDTO.getAttemptedAt());
        loginAttempt.setSuccess(loginAttemptDTO.getSuccess());
        final User user = loginAttemptDTO.getUser() == null ? null : userRepository.findById(loginAttemptDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        loginAttempt.setUser(user);
        return loginAttempt;
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final LoginAttempt userLoginAttempt = loginAttemptRepository.findFirstByUserId(event.getId());
        if (userLoginAttempt != null) {
            referencedException.setKey("user.loginAttempt.user.referenced");
            referencedException.addParam(userLoginAttempt.getId());
            throw referencedException;
        }
    }

}
