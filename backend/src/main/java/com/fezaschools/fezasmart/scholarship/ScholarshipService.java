package com.fezaschools.fezasmart.scholarship;

import com.fezaschools.fezasmart.discount.Discount;
import com.fezaschools.fezasmart.discount.DiscountRepository;
import com.fezaschools.fezasmart.events.BeforeDeleteDiscount;
import com.fezaschools.fezasmart.events.BeforeDeleteScholarship;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ScholarshipService {

    private final ScholarshipRepository scholarshipRepository;
    private final DiscountRepository discountRepository;
    private final ApplicationEventPublisher publisher;

    public ScholarshipService(final ScholarshipRepository scholarshipRepository,
            final DiscountRepository discountRepository,
            final ApplicationEventPublisher publisher) {
        this.scholarshipRepository = scholarshipRepository;
        this.discountRepository = discountRepository;
        this.publisher = publisher;
    }

    public List<ScholarshipDTO> findAll() {
        final List<Scholarship> scholarships = scholarshipRepository.findAll(Sort.by("id"));
        return scholarships.stream()
                .map(scholarship -> mapToDTO(scholarship, new ScholarshipDTO()))
                .toList();
    }

    public ScholarshipDTO get(final Integer id) {
        return scholarshipRepository.findById(id)
                .map(scholarship -> mapToDTO(scholarship, new ScholarshipDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ScholarshipDTO scholarshipDTO) {
        final Scholarship scholarship = new Scholarship();
        mapToEntity(scholarshipDTO, scholarship);
        return scholarshipRepository.save(scholarship).getId();
    }

    public void update(final Integer id, final ScholarshipDTO scholarshipDTO) {
        final Scholarship scholarship = scholarshipRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(scholarshipDTO, scholarship);
        scholarshipRepository.save(scholarship);
    }

    public void delete(final Integer id) {
        final Scholarship scholarship = scholarshipRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteScholarship(id));
        scholarshipRepository.delete(scholarship);
    }

    private ScholarshipDTO mapToDTO(final Scholarship scholarship,
            final ScholarshipDTO scholarshipDTO) {
        scholarshipDTO.setId(scholarship.getId());
        scholarshipDTO.setName(scholarship.getName());
        scholarshipDTO.setDescription(scholarship.getDescription());
        scholarshipDTO.setStartDate(scholarship.getStartDate());
        scholarshipDTO.setEndDate(scholarship.getEndDate());
        scholarshipDTO.setIsActive(scholarship.getIsActive());
        scholarshipDTO.setDiscount(scholarship.getDiscount() == null ? null : scholarship.getDiscount().getId());
        return scholarshipDTO;
    }

    private Scholarship mapToEntity(final ScholarshipDTO scholarshipDTO,
            final Scholarship scholarship) {
        scholarship.setName(scholarshipDTO.getName());
        scholarship.setDescription(scholarshipDTO.getDescription());
        scholarship.setStartDate(scholarshipDTO.getStartDate());
        scholarship.setEndDate(scholarshipDTO.getEndDate());
        scholarship.setIsActive(scholarshipDTO.getIsActive());
        final Discount discount = scholarshipDTO.getDiscount() == null ? null : discountRepository.findById(scholarshipDTO.getDiscount())
                .orElseThrow(() -> new NotFoundException("discount not found"));
        scholarship.setDiscount(discount);
        return scholarship;
    }

    @EventListener(BeforeDeleteDiscount.class)
    public void on(final BeforeDeleteDiscount event) {
        final ReferencedException referencedException = new ReferencedException();
        final Scholarship discountScholarship = scholarshipRepository.findFirstByDiscountId(event.getId());
        if (discountScholarship != null) {
            referencedException.setKey("discount.scholarship.discount.referenced");
            referencedException.addParam(discountScholarship.getId());
            throw referencedException;
        }
    }

}
