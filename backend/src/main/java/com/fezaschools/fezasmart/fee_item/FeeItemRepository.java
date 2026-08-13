package com.fezaschools.fezasmart.fee_item;

import org.springframework.data.jpa.repository.JpaRepository;


public interface FeeItemRepository extends JpaRepository<FeeItem, Integer> {

    FeeItem findFirstByFeeStructureId(Integer id);

    FeeItem findFirstBySubjectId(Integer id);

    FeeItem findFirstByClasssId(Integer id);

}
