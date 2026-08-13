package com.fezaschools.fezasmart.student_point;

import com.fezaschools.fezasmart.student.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class StudentPoint {

    @Id
    @Column(nullable = false, updatable = false)
    private String pointType;

    @Column(precision = 5, scale = 1)
    private BigDecimal currentPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

}
