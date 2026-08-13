package com.fezaschools.fezasmart.grade_boundary;

import com.fezaschools.fezasmart.exam.Exam;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.subject.Subject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class GradeBoundary {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal minPercentage;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal maxPercentage;

    @Column(length = 5)
    private String letterGrade;

    @Column(precision = 3, scale = 1)
    private BigDecimal pointGrade;

    @Column
    private String remark;

    @Column(nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

}
