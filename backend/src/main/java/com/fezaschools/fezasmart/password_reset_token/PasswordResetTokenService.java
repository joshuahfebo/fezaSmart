package com.fezaschools.fezasmart.password_reset_token;

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
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;

    public PasswordResetTokenService(
            final PasswordResetTokenRepository passwordResetTokenRepository,
            final UserRepository userRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
    }

    public List<PasswordResetTokenDTO> findAll() {
        final List<PasswordResetToken> passwordResetTokens = passwordResetTokenRepository.findAll(Sort.by("id"));
        return passwordResetTokens.stream()
                .map(passwordResetToken -> mapToDTO(passwordResetToken, new PasswordResetTokenDTO()))
                .toList();
    }

    public PasswordResetTokenDTO get(final Integer id) {
        return passwordResetTokenRepository.findById(id)
                .map(passwordResetToken -> mapToDTO(passwordResetToken, new PasswordResetTokenDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PasswordResetTokenDTO passwordResetTokenDTO) {
        final PasswordResetToken passwordResetToken = new PasswordResetToken();
        mapToEntity(passwordResetTokenDTO, passwordResetToken);
        return passwordResetTokenRepository.save(passwordResetToken).getId();
    }

    public void update(final Integer id, final PasswordResetTokenDTO passwordResetTokenDTO) {
        final PasswordResetToken passwordResetToken = passwordResetTokenRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(passwordResetTokenDTO, passwordResetToken);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public void delete(final Integer id) {
        final PasswordResetToken passwordResetToken = passwordResetTokenRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        passwordResetTokenRepository.delete(passwordResetToken);
    }

    private PasswordResetTokenDTO mapToDTO(final PasswordResetToken passwordResetToken,
            final PasswordResetTokenDTO passwordResetTokenDTO) {
        passwordResetTokenDTO.setId(passwordResetToken.getId());
        passwordResetTokenDTO.setToken(passwordResetToken.getToken());
        passwordResetTokenDTO.setExpiresAt(passwordResetToken.getExpiresAt());
        passwordResetTokenDTO.setUsed(passwordResetToken.getUsed());
        passwordResetTokenDTO.setCreatedAt(passwordResetToken.getCreatedAt());
        passwordResetTokenDTO.setUser(passwordResetToken.getUser() == null ? null : passwordResetToken.getUser().getId());
        return passwordResetTokenDTO;
    }

    private PasswordResetToken mapToEntity(final PasswordResetTokenDTO passwordResetTokenDTO,
            final PasswordResetToken passwordResetToken) {
        passwordResetToken.setToken(passwordResetTokenDTO.getToken());
        passwordResetToken.setExpiresAt(passwordResetTokenDTO.getExpiresAt());
        passwordResetToken.setUsed(passwordResetTokenDTO.getUsed());
        passwordResetToken.setCreatedAt(passwordResetTokenDTO.getCreatedAt());
        final User user = passwordResetTokenDTO.getUser() == null ? null : userRepository.findById(passwordResetTokenDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        passwordResetToken.setUser(user);
        return passwordResetToken;
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final PasswordResetToken userPasswordResetToken = passwordResetTokenRepository.findFirstByUserId(event.getId());
        if (userPasswordResetToken != null) {
            referencedException.setKey("user.passwordResetToken.user.referenced");
            referencedException.addParam(userPasswordResetToken.getId());
            throw referencedException;
        }
    }

}
