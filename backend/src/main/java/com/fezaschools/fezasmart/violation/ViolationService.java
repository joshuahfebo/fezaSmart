package com.fezaschools.fezasmart.violation;

import com.fezaschools.fezasmart.events.BeforeDeleteViolation;
import com.fezaschools.fezasmart.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ViolationService {

    private final ViolationRepository violationRepository;
    private final ApplicationEventPublisher publisher;

    public ViolationService(final ViolationRepository violationRepository,
            final ApplicationEventPublisher publisher) {
        this.violationRepository = violationRepository;
        this.publisher = publisher;
    }

    public List<ViolationDTO> findAll() {
        final List<Violation> violations = violationRepository.findAll(Sort.by("id"));
        return violations.stream()
                .map(violation -> mapToDTO(violation, new ViolationDTO()))
                .toList();
    }

    public ViolationDTO get(final Integer id) {
        return violationRepository.findById(id)
                .map(violation -> mapToDTO(violation, new ViolationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ViolationDTO violationDTO) {
        final Violation violation = new Violation();
        mapToEntity(violationDTO, violation);
        return violationRepository.save(violation).getId();
    }

    public void update(final Integer id, final ViolationDTO violationDTO) {
        final Violation violation = violationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(violationDTO, violation);
        violationRepository.save(violation);
    }

    public void delete(final Integer id) {
        final Violation violation = violationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteViolation(id));
        violationRepository.delete(violation);
    }

    private ViolationDTO mapToDTO(final Violation violation, final ViolationDTO violationDTO) {
        violationDTO.setId(violation.getId());
        violationDTO.setName(violation.getName());
        violationDTO.setDescription(violation.getDescription());
        violationDTO.setPointDeduction(violation.getPointDeduction());
        violationDTO.setPointType(violation.getPointType());
        return violationDTO;
    }

    private Violation mapToEntity(final ViolationDTO violationDTO, final Violation violation) {
        violation.setName(violationDTO.getName());
        violation.setDescription(violationDTO.getDescription());
        violation.setPointDeduction(violationDTO.getPointDeduction());
        violation.setPointType(violationDTO.getPointType());
        return violation;
    }

}
