package com.fezaschools.fezasmart.session;

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
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public SessionService(final SessionRepository sessionRepository,
            final UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public List<SessionDTO> findAll() {
        final List<Session> sessions = sessionRepository.findAll(Sort.by("id"));
        return sessions.stream()
                .map(session -> mapToDTO(session, new SessionDTO()))
                .toList();
    }

    public SessionDTO get(final Integer id) {
        return sessionRepository.findById(id)
                .map(session -> mapToDTO(session, new SessionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final SessionDTO sessionDTO) {
        final Session session = new Session();
        mapToEntity(sessionDTO, session);
        return sessionRepository.save(session).getId();
    }

    public void update(final Integer id, final SessionDTO sessionDTO) {
        final Session session = sessionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(sessionDTO, session);
        sessionRepository.save(session);
    }

    public void delete(final Integer id) {
        final Session session = sessionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        sessionRepository.delete(session);
    }

    private SessionDTO mapToDTO(final Session session, final SessionDTO sessionDTO) {
        sessionDTO.setId(session.getId());
        sessionDTO.setToken(session.getToken());
        sessionDTO.setRefreshToken(session.getRefreshToken());
        sessionDTO.setTokenType(session.getTokenType());
        sessionDTO.setDeviceInfo(session.getDeviceInfo());
        sessionDTO.setIpAddress(session.getIpAddress());
        sessionDTO.setExpiresAt(session.getExpiresAt());
        sessionDTO.setCreatedAt(session.getCreatedAt());
        sessionDTO.setRevoked(session.getRevoked());
        sessionDTO.setUser(session.getUser() == null ? null : session.getUser().getId());
        return sessionDTO;
    }

    private Session mapToEntity(final SessionDTO sessionDTO, final Session session) {
        session.setToken(sessionDTO.getToken());
        session.setRefreshToken(sessionDTO.getRefreshToken());
        session.setTokenType(sessionDTO.getTokenType());
        session.setDeviceInfo(sessionDTO.getDeviceInfo());
        session.setIpAddress(sessionDTO.getIpAddress());
        session.setExpiresAt(sessionDTO.getExpiresAt());
        session.setCreatedAt(sessionDTO.getCreatedAt());
        session.setRevoked(sessionDTO.getRevoked());
        final User user = sessionDTO.getUser() == null ? null : userRepository.findById(sessionDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        session.setUser(user);
        return session;
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final Session userSession = sessionRepository.findFirstByUserId(event.getId());
        if (userSession != null) {
            referencedException.setKey("user.session.user.referenced");
            referencedException.addParam(userSession.getId());
            throw referencedException;
        }
    }

}
