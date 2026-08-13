package com.fezaschools.fezasmart.exam;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ExamRepository extends JpaRepository<Exam, Integer> {

    Exam findFirstByTermId(Integer id);

}
