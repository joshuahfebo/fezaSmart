package com.fezaschools.fezasmart.result;

import com.fezaschools.fezasmart.exam.Exam;
import com.fezaschools.fezasmart.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ResultRepository extends JpaRepository<Result, Integer> {

    Result findFirstByStudentId(Integer id);

    Result findFirstByExamId(Integer id);

    Optional<Result> findByStudentAndExam(Student student, Exam exam);

    List<Result> findByExam(Exam exam);

    List<Result> findByStudentId(Integer studentId);

    @Query("SELECT r FROM Result r WHERE r.exam.id = :examId ORDER BY r.totalScore DESC")
    List<Result> findByExamIdOrderByTotalScoreDesc(@Param("examId") Integer examId);
}
