package com.fezaschools.fezasmart.student_scholarship;

import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentScholarshipRepository extends JpaRepository<StudentScholarship, Integer> {

    StudentScholarship findFirstByStudentId(Integer id);

    StudentScholarship findFirstByScholarshipId(Integer id);

    StudentScholarship findFirstByAwardedById(Integer id);

}
