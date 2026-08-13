package com.fezaschools.fezasmart.student_enrollment;

import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, Integer> {

    StudentEnrollment findFirstByStudentId(Integer id);

    StudentEnrollment findFirstByClasssId(Integer id);

    StudentEnrollment findFirstByAcademicYearId(Integer id);

}
