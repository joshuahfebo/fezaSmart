package com.fezaschools.fezasmart.scholarship;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ScholarshipRepository extends JpaRepository<Scholarship, Integer> {

    Scholarship findFirstByDiscountId(Integer id);

}
