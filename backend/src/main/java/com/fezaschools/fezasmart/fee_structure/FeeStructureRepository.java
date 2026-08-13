package com.fezaschools.fezasmart.fee_structure;

import org.springframework.data.jpa.repository.JpaRepository;


public interface FeeStructureRepository extends JpaRepository<FeeStructure, Integer> {

    FeeStructure findFirstBySchoolId(Integer id);

    FeeStructure findFirstByAcademicYearId(Integer id);

}
