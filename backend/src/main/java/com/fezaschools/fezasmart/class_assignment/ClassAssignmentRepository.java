package com.fezaschools.fezasmart.class_assignment;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ClassAssignmentRepository extends JpaRepository<ClassAssignment, Integer> {

    ClassAssignment findFirstByClasssId(Integer id);

    ClassAssignment findFirstByStaffId(Integer id);

}
