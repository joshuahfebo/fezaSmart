package com.fezaschools.fezasmart.fee_structure;

import com.fezaschools.fezasmart.academic_year.AcademicYear;
import com.fezaschools.fezasmart.fee_item.FeeItem;
import com.fezaschools.fezasmart.invoice.Invoice;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.student_fee_assignment.StudentFeeAssignment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class FeeStructure {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "longtext", name = "\"description\"")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @OneToMany(mappedBy = "feeStructure")
    private Set<FeeItem> feeStructureFeeItems = new HashSet<>();

    @OneToMany(mappedBy = "feeStructure")
    private Set<StudentFeeAssignment> feeStructureStudentFeeAssignments = new HashSet<>();

    @OneToMany(mappedBy = "feeStructure")
    private Set<Invoice> feeStructureInvoices = new HashSet<>();

}
