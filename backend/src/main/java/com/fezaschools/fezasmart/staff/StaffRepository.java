package com.fezaschools.fezasmart.staff;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StaffRepository extends JpaRepository<Staff, Integer> {

    Staff findFirstBySchoolId(Integer id);

    Staff findFirstByUserId(Integer id);

    java.util.Optional<Staff> findOptionalByUserId(Integer id);

    Staff findFirstByDepartmentId(Integer id);

    List<Staff> findAllByTeacherSubjectSubjectsId(Integer id);

}
