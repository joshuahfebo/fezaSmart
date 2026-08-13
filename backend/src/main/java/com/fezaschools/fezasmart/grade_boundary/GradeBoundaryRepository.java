package com.fezaschools.fezasmart.grade_boundary;

import com.fezaschools.fezasmart.exam.Exam;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;


public interface GradeBoundaryRepository extends JpaRepository<GradeBoundary, Integer> {

    GradeBoundary findFirstBySchoolId(Integer id);

    GradeBoundary findFirstBySubjectId(Integer id);

    GradeBoundary findFirstByExamId(Integer id);

    List<GradeBoundary> findBySchool(School school);

    List<GradeBoundary> findBySubject(Subject subject);

    List<GradeBoundary> findByExam(Exam exam);

    @Query("SELECT gb FROM GradeBoundary gb WHERE gb.school.id = :schoolId AND gb.minPercentage <= :percentage AND gb.maxPercentage >= :percentage")
    List<GradeBoundary> findBySchoolAndPercentage(@Param("schoolId") Integer schoolId, @Param("percentage") BigDecimal percentage);

    @Query("SELECT gb FROM GradeBoundary gb WHERE gb.type = :type AND gb.minPercentage <= :percentage AND gb.maxPercentage >= :percentage")
    GradeBoundary findByTypeAndPercentage(@Param("type") String type, @Param("percentage") BigDecimal percentage);
}
