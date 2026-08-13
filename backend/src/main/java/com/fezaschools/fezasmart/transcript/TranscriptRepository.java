package com.fezaschools.fezasmart.transcript;

import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.academic_year.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TranscriptRepository extends JpaRepository<Transcript, Integer> {

    Transcript findFirstByStudentId(Integer id);

    Transcript findFirstByAcademicYearId(Integer id);

    Transcript findFirstByGeneratedById(Integer id);

    List<Transcript> findByStudent(Student student);

    List<Transcript> findByAcademicYear(AcademicYear academicYear);

    Optional<Transcript> findByStudentAndAcademicYear(Student student, AcademicYear academicYear);
}
