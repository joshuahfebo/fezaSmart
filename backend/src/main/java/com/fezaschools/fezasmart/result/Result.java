package com.fezaschools.fezasmart.result;

import com.fezaschools.fezasmart.exam.Exam;
import com.fezaschools.fezasmart.student.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Result {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(precision = 7, scale = 2)
    private BigDecimal totalScore;

    @Column(precision = 5, scale = 2)
    private BigDecimal averagePercentage;

    @Column(precision = 5, scale = 2)
    private BigDecimal totalPoints;

    @Column
    private String division;

    @Column
    private Integer rankInClass;

    @Column(columnDefinition = "longtext")
    private String remark;

    @Column
    private OffsetDateTime computedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

}
