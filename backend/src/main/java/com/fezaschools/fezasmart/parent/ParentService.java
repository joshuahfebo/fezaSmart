package com.fezaschools.fezasmart.parent;

import com.fezaschools.fezasmart.events.BeforeDeleteParent;
import com.fezaschools.fezasmart.events.BeforeDeleteUser;
import com.fezaschools.fezasmart.user.User;
import com.fezaschools.fezasmart.user.UserRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class ParentService {

    private final ParentRepository parentRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;

    public ParentService(final ParentRepository parentRepository,
            final UserRepository userRepository, final ApplicationEventPublisher publisher) {
        this.parentRepository = parentRepository;
        this.userRepository = userRepository;
        this.publisher = publisher;
    }

    public List<ParentDTO> findAll() {
        final List<Parent> parents = parentRepository.findAll(Sort.by("id"));
        return parents.stream()
                .map(parent -> mapToDTO(parent, new ParentDTO()))
                .toList();
    }

    public ParentDTO get(final Integer id) {
        return parentRepository.findById(id)
                .map(parent -> mapToDTO(parent, new ParentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ParentDTO parentDTO) {
        final Parent parent = new Parent();
        mapToEntity(parentDTO, parent);
        return parentRepository.save(parent).getId();
    }

    public void update(final Integer id, final ParentDTO parentDTO) {
        final Parent parent = parentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(parentDTO, parent);
        parentRepository.save(parent);
    }

    public void delete(final Integer id) {
        final Parent parent = parentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteParent(id));
        parentRepository.delete(parent);
    }

    private ParentDTO mapToDTO(final Parent parent, final ParentDTO parentDTO) {
        parentDTO.setId(parent.getId());
        parentDTO.setFirstName(parent.getFirstName());
        parentDTO.setLastName(parent.getLastName());
        parentDTO.setRelationshipType(parent.getRelationshipType());
        parentDTO.setGender(parent.getGender());
        parentDTO.setDob(parent.getDob());
        parentDTO.setCreatedAt(parent.getCreatedAt());
        parentDTO.setUpdatedAt(parent.getUpdatedAt());
        parentDTO.setDeletedAt(parent.getDeletedAt());
        parentDTO.setDeletedBy(parent.getDeletedBy());
        parentDTO.setRestoreToken(parent.getRestoreToken());
        parentDTO.setUser(parent.getUser() == null ? null : parent.getUser().getId());
        return parentDTO;
    }

    private Parent mapToEntity(final ParentDTO parentDTO, final Parent parent) {
        parent.setFirstName(parentDTO.getFirstName());
        parent.setLastName(parentDTO.getLastName());
        parent.setRelationshipType(parentDTO.getRelationshipType());
        parent.setGender(parentDTO.getGender());
        parent.setDob(parentDTO.getDob());
        parent.setCreatedAt(parentDTO.getCreatedAt());
        parent.setUpdatedAt(parentDTO.getUpdatedAt());
        parent.setDeletedAt(parentDTO.getDeletedAt());
        parent.setDeletedBy(parentDTO.getDeletedBy());
        parent.setRestoreToken(parentDTO.getRestoreToken());
        final User user = parentDTO.getUser() == null ? null : userRepository.findById(parentDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        parent.setUser(user);
        return parent;
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final Parent userParent = parentRepository.findFirstByUserId(event.getId());
        if (userParent != null) {
            referencedException.setKey("user.parent.user.referenced");
            referencedException.addParam(userParent.getId());
            throw referencedException;
        }
    }

}
