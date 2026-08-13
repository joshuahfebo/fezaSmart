package com.fezaschools.fezasmart.student;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findFirstBySchoolId(Integer id);

    Student findFirstByUserId(Integer id);

    java.util.Optional<Student> findOptionalByUserId(Integer id);

    List<Student> findAllByStudentParentParentsId(Integer id);

}
