package com.fezaschools.fezasmart.academic_year;

import com.fezaschools.fezasmart.classs.Classs;
import com.fezaschools.fezasmart.fee_structure.FeeStructure;
import com.fezaschools.fezasmart.invoice.Invoice;
import com.fezaschools.fezasmart.student_enrollment.StudentEnrollment;
import com.fezaschools.fezasmart.timetable.Timetable;
import com.fezaschools.fezasmart.transcript.Transcript;
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
public class AcademicYear {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column
    private Boolean isCurrent;

    @OneToMany(mappedBy = "academicYear")
    private Set<Classs> academicYearClasses = new HashSet<>();

    @OneToMany(mappedBy = "academicYear")
    private Set<StudentEnrollment> academicYearStudentEnrollments = new HashSet<>();

    @OneToMany(mappedBy = "academicYear")
    private Set<Timetable> academicYearTimetables = new HashSet<>();

    @OneToMany(mappedBy = "academicYear")
    private Set<Transcript> academicYearTranscripts = new HashSet<>();

    @OneToMany(mappedBy = "academicYear")
    private Set<FeeStructure> academicYearFeeStructures = new HashSet<>();

    @OneToMany(mappedBy = "academicYear")
    private Set<Invoice> academicYearInvoices = new HashSet<>();

}
