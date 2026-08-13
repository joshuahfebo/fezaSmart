package com.fezaschools.fezasmart.exam;

import com.fezaschools.fezasmart.exam_subject.ExamSubject;
import com.fezaschools.fezasmart.grade_boundary.GradeBoundary;
import com.fezaschools.fezasmart.result.Result;
import com.fezaschools.fezasmart.term.Term;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Exam {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private LocalDate examDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;

    @OneToMany(mappedBy = "exam")
    private Set<ExamSubject> examExamSubjects = new HashSet<>();

    @OneToMany(mappedBy = "exam")
    private Set<GradeBoundary> examGradeBoundaries = new HashSet<>();

    @OneToMany(mappedBy = "exam")
    private Set<Result> examResults = new HashSet<>();

}
