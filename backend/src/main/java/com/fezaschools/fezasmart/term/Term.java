package com.fezaschools.fezasmart.term;

import com.fezaschools.fezasmart.exam.Exam;
import com.fezaschools.fezasmart.invoice.Invoice;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Term {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer academicYearId;

    @Column(nullable = false)
    private Integer termNumber;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "term")
    private Set<Exam> termExams = new HashSet<>();

    @OneToMany(mappedBy = "term")
    private Set<Invoice> termInvoices = new HashSet<>();

}
