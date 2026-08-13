package com.fezaschools.fezasmart.exam_subject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ExamSubjectRepository extends JpaRepository<ExamSubject, Integer> {

    ExamSubject findFirstByExamId(Integer id);

    ExamSubject findFirstBySubjectId(Integer id);

    List<ExamSubject> findByExamId(Integer examId);
}
