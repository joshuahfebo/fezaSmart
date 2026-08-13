package com.fezaschools.fezasmart.student_fee_assignment;

import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentFeeAssignmentRepository extends JpaRepository<StudentFeeAssignment, Integer> {

    StudentFeeAssignment findFirstByStudentId(Integer id);

    StudentFeeAssignment findFirstByFeeStructureId(Integer id);

    StudentFeeAssignment findFirstByAssignedById(Integer id);

}
