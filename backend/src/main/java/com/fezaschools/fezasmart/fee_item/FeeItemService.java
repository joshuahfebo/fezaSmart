package com.fezaschools.fezasmart.fee_item;

import com.fezaschools.fezasmart.classs.Classs;
import com.fezaschools.fezasmart.classs.ClasssRepository;
import com.fezaschools.fezasmart.events.BeforeDeleteClasss;
import com.fezaschools.fezasmart.events.BeforeDeleteFeeItem;
import com.fezaschools.fezasmart.events.BeforeDeleteFeeStructure;
import com.fezaschools.fezasmart.events.BeforeDeleteSubject;
import com.fezaschools.fezasmart.fee_structure.FeeStructure;
import com.fezaschools.fezasmart.fee_structure.FeeStructureRepository;
import com.fezaschools.fezasmart.subject.Subject;
import com.fezaschools.fezasmart.subject.SubjectRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class FeeItemService {

    private final FeeItemRepository feeItemRepository;
    private final FeeStructureRepository feeStructureRepository;
    private final SubjectRepository subjectRepository;
    private final ClasssRepository classsRepository;
    private final ApplicationEventPublisher publisher;

    public FeeItemService(final FeeItemRepository feeItemRepository,
            final FeeStructureRepository feeStructureRepository,
            final SubjectRepository subjectRepository, final ClasssRepository classsRepository,
            final ApplicationEventPublisher publisher) {
        this.feeItemRepository = feeItemRepository;
        this.feeStructureRepository = feeStructureRepository;
        this.subjectRepository = subjectRepository;
        this.classsRepository = classsRepository;
        this.publisher = publisher;
    }

    public List<FeeItemDTO> findAll() {
        final List<FeeItem> feeItems = feeItemRepository.findAll(Sort.by("id"));
        return feeItems.stream()
                .map(feeItem -> mapToDTO(feeItem, new FeeItemDTO()))
                .toList();
    }

    public FeeItemDTO get(final Integer id) {
        return feeItemRepository.findById(id)
                .map(feeItem -> mapToDTO(feeItem, new FeeItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final FeeItemDTO feeItemDTO) {
        final FeeItem feeItem = new FeeItem();
        mapToEntity(feeItemDTO, feeItem);
        return feeItemRepository.save(feeItem).getId();
    }

    public void update(final Integer id, final FeeItemDTO feeItemDTO) {
        final FeeItem feeItem = feeItemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(feeItemDTO, feeItem);
        feeItemRepository.save(feeItem);
    }

    public void delete(final Integer id) {
        final FeeItem feeItem = feeItemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteFeeItem(id));
        feeItemRepository.delete(feeItem);
    }

    private FeeItemDTO mapToDTO(final FeeItem feeItem, final FeeItemDTO feeItemDTO) {
        feeItemDTO.setId(feeItem.getId());
        feeItemDTO.setName(feeItem.getName());
        feeItemDTO.setAmount(feeItem.getAmount());
        feeItemDTO.setIsOptional(feeItem.getIsOptional());
        feeItemDTO.setFeeStructure(feeItem.getFeeStructure() == null ? null : feeItem.getFeeStructure().getId());
        feeItemDTO.setSubject(feeItem.getSubject() == null ? null : feeItem.getSubject().getId());
        feeItemDTO.setClasss(feeItem.getClasss() == null ? null : feeItem.getClasss().getId());
        return feeItemDTO;
    }

    private FeeItem mapToEntity(final FeeItemDTO feeItemDTO, final FeeItem feeItem) {
        feeItem.setName(feeItemDTO.getName());
        feeItem.setAmount(feeItemDTO.getAmount());
        feeItem.setIsOptional(feeItemDTO.getIsOptional());
        final FeeStructure feeStructure = feeItemDTO.getFeeStructure() == null ? null : feeStructureRepository.findById(feeItemDTO.getFeeStructure())
                .orElseThrow(() -> new NotFoundException("feeStructure not found"));
        feeItem.setFeeStructure(feeStructure);
        final Subject subject = feeItemDTO.getSubject() == null ? null : subjectRepository.findById(feeItemDTO.getSubject())
                .orElseThrow(() -> new NotFoundException("subject not found"));
        feeItem.setSubject(subject);
        final Classs classs = feeItemDTO.getClasss() == null ? null : classsRepository.findById(feeItemDTO.getClasss())
                .orElseThrow(() -> new NotFoundException("classs not found"));
        feeItem.setClasss(classs);
        return feeItem;
    }

    @EventListener(BeforeDeleteFeeStructure.class)
    public void on(final BeforeDeleteFeeStructure event) {
        final ReferencedException referencedException = new ReferencedException();
        final FeeItem feeStructureFeeItem = feeItemRepository.findFirstByFeeStructureId(event.getId());
        if (feeStructureFeeItem != null) {
            referencedException.setKey("feeStructure.feeItem.feeStructure.referenced");
            referencedException.addParam(feeStructureFeeItem.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteSubject.class)
    public void on(final BeforeDeleteSubject event) {
        final ReferencedException referencedException = new ReferencedException();
        final FeeItem subjectFeeItem = feeItemRepository.findFirstBySubjectId(event.getId());
        if (subjectFeeItem != null) {
            referencedException.setKey("subject.feeItem.subject.referenced");
            referencedException.addParam(subjectFeeItem.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteClasss.class)
    public void on(final BeforeDeleteClasss event) {
        final ReferencedException referencedException = new ReferencedException();
        final FeeItem classsFeeItem = feeItemRepository.findFirstByClasssId(event.getId());
        if (classsFeeItem != null) {
            referencedException.setKey("classs.feeItem.classs.referenced");
            referencedException.addParam(classsFeeItem.getId());
            throw referencedException;
        }
    }

}
