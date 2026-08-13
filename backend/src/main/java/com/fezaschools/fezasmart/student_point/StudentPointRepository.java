package com.fezaschools.fezasmart.student_point;

import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentPointRepository extends JpaRepository<StudentPoint, String> {

    StudentPoint findFirstByStudentId(Integer id);

    boolean existsByPointTypeIgnoreCase(String pointType);

}
