package com.fezaschools.fezasmart.email_verification_token;

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
public class EmailVerificationTokenService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UserRepository userRepository;

    public EmailVerificationTokenService(
            final EmailVerificationTokenRepository emailVerificationTokenRepository,
            final UserRepository userRepository) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.userRepository = userRepository;
    }

    public List<EmailVerificationTokenDTO> findAll() {
        final List<EmailVerificationToken> emailVerificationTokens = emailVerificationTokenRepository.findAll(Sort.by("id"));
        return emailVerificationTokens.stream()
                .map(emailVerificationToken -> mapToDTO(emailVerificationToken, new EmailVerificationTokenDTO()))
                .toList();
    }

    public EmailVerificationTokenDTO get(final Integer id) {
        return emailVerificationTokenRepository.findById(id)
                .map(emailVerificationToken -> mapToDTO(emailVerificationToken, new EmailVerificationTokenDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final EmailVerificationTokenDTO emailVerificationTokenDTO) {
        final EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        mapToEntity(emailVerificationTokenDTO, emailVerificationToken);
        return emailVerificationTokenRepository.save(emailVerificationToken).getId();
    }

    public void update(final Integer id,
            final EmailVerificationTokenDTO emailVerificationTokenDTO) {
        final EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(emailVerificationTokenDTO, emailVerificationToken);
        emailVerificationTokenRepository.save(emailVerificationToken);
    }

    public void delete(final Integer id) {
        final EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        emailVerificationTokenRepository.delete(emailVerificationToken);
    }

    private EmailVerificationTokenDTO mapToDTO(final EmailVerificationToken emailVerificationToken,
            final EmailVerificationTokenDTO emailVerificationTokenDTO) {
        emailVerificationTokenDTO.setId(emailVerificationToken.getId());
        emailVerificationTokenDTO.setToken(emailVerificationToken.getToken());
        emailVerificationTokenDTO.setExpiresAt(emailVerificationToken.getExpiresAt());
        emailVerificationTokenDTO.setUsed(emailVerificationToken.getUsed());
        emailVerificationTokenDTO.setCreatedAt(emailVerificationToken.getCreatedAt());
        emailVerificationTokenDTO.setUser(emailVerificationToken.getUser() == null ? null : emailVerificationToken.getUser().getId());
        return emailVerificationTokenDTO;
    }

    private EmailVerificationToken mapToEntity(
            final EmailVerificationTokenDTO emailVerificationTokenDTO,
            final EmailVerificationToken emailVerificationToken) {
        emailVerificationToken.setToken(emailVerificationTokenDTO.getToken());
        emailVerificationToken.setExpiresAt(emailVerificationTokenDTO.getExpiresAt());
        emailVerificationToken.setUsed(emailVerificationTokenDTO.getUsed());
        emailVerificationToken.setCreatedAt(emailVerificationTokenDTO.getCreatedAt());
        final User user = emailVerificationTokenDTO.getUser() == null ? null : userRepository.findById(emailVerificationTokenDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        emailVerificationToken.setUser(user);
        return emailVerificationToken;
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final EmailVerificationToken userEmailVerificationToken = emailVerificationTokenRepository.findFirstByUserId(event.getId());
        if (userEmailVerificationToken != null) {
            referencedException.setKey("user.emailVerificationToken.user.referenced");
            referencedException.addParam(userEmailVerificationToken.getId());
            throw referencedException;
        }
    }

}
