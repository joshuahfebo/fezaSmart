package com.fezaschools.fezasmart.invoice;

import com.fezaschools.fezasmart.academic_year.AcademicYear;
import com.fezaschools.fezasmart.fee_structure.FeeStructure;
import com.fezaschools.fezasmart.invoice_item.InvoiceItem;
import com.fezaschools.fezasmart.payment.Payment;
import com.fezaschools.fezasmart.payment_allocation.PaymentAllocation;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.student.Student;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Invoice {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String invoiceNumber;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal paidAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal balance;

    @Column
    private String status;

    @Column
    private LocalDate issuedDate;

    @Column
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_structure_id")
    private FeeStructure feeStructure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id")
    private AcademicYear academicYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id")
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_by_id")
    private Staff issuedBy;

    @OneToMany(mappedBy = "invoice")
    private Set<InvoiceItem> invoiceInvoiceItems = new HashSet<>();

    @OneToMany(mappedBy = "invoice")
    private Set<Payment> invoicePayments = new HashSet<>();

    @OneToMany(mappedBy = "invoice")
    private Set<PaymentAllocation> invoicePaymentAllocations = new HashSet<>();

}
