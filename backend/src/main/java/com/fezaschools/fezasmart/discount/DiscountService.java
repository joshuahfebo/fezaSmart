package com.fezaschools.fezasmart.discount;

import com.fezaschools.fezasmart.events.BeforeDeleteDiscount;
import com.fezaschools.fezasmart.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final ApplicationEventPublisher publisher;

    public DiscountService(final DiscountRepository discountRepository,
            final ApplicationEventPublisher publisher) {
        this.discountRepository = discountRepository;
        this.publisher = publisher;
    }

    public List<DiscountDTO> findAll() {
        final List<Discount> discounts = discountRepository.findAll(Sort.by("id"));
        return discounts.stream()
                .map(discount -> mapToDTO(discount, new DiscountDTO()))
                .toList();
    }

    public DiscountDTO get(final Integer id) {
        return discountRepository.findById(id)
                .map(discount -> mapToDTO(discount, new DiscountDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final DiscountDTO discountDTO) {
        final Discount discount = new Discount();
        mapToEntity(discountDTO, discount);
        return discountRepository.save(discount).getId();
    }

    public void update(final Integer id, final DiscountDTO discountDTO) {
        final Discount discount = discountRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(discountDTO, discount);
        discountRepository.save(discount);
    }

    public void delete(final Integer id) {
        final Discount discount = discountRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteDiscount(id));
        discountRepository.delete(discount);
    }

    private DiscountDTO mapToDTO(final Discount discount, final DiscountDTO discountDTO) {
        discountDTO.setId(discount.getId());
        discountDTO.setName(discount.getName());
        discountDTO.setDescription(discount.getDescription());
        discountDTO.setDiscountType(discount.getDiscountType());
        discountDTO.setValue(discount.getValue());
        discountDTO.setStartDate(discount.getStartDate());
        discountDTO.setEndDate(discount.getEndDate());
        discountDTO.setIsActive(discount.getIsActive());
        return discountDTO;
    }

    private Discount mapToEntity(final DiscountDTO discountDTO, final Discount discount) {
        discount.setName(discountDTO.getName());
        discount.setDescription(discountDTO.getDescription());
        discount.setDiscountType(discountDTO.getDiscountType());
        discount.setValue(discountDTO.getValue());
        discount.setStartDate(discountDTO.getStartDate());
        discount.setEndDate(discountDTO.getEndDate());
        discount.setIsActive(discountDTO.getIsActive());
        return discount;
    }

}
