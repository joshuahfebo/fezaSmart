package com.fezaschools.fezasmart.student_score;

import com.fezaschools.fezasmart.exam_subject.ExamSubject;
import com.fezaschools.fezasmart.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface StudentScoreRepository extends JpaRepository<StudentScore, Integer> {

    StudentScore findFirstByStudentId(Integer id);

    StudentScore findFirstByExamSubjectId(Integer id);

    List<StudentScore> findByExamSubject(ExamSubject examSubject);

    List<StudentScore> findByStudent(Student student);

    @Query("SELECT ss FROM StudentScore ss WHERE ss.examSubject.exam.id = :examId AND ss.student.id = :studentId")
    List<StudentScore> findByExamIdAndStudentId(@Param("examId") Integer examId, @Param("studentId") Integer studentId);

    @Query("SELECT ss FROM StudentScore ss WHERE ss.examSubject.exam.id = :examId")
    List<StudentScore> findByExamId(@Param("examId") Integer examId);
}
