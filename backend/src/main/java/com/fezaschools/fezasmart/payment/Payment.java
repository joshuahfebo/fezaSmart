package com.fezaschools.fezasmart.payment;

import com.fezaschools.fezasmart.invoice.Invoice;
import com.fezaschools.fezasmart.payment_allocation.PaymentAllocation;
import com.fezaschools.fezasmart.receipt.Receipt;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.user.User;
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
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Payment {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String paymentNumber;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column
    private String paymentMethod;

    @Column
    private String transactionReference;

    @Column
    private OffsetDateTime paymentDate;

    @Column
    private String status;

    @Column
    private OffsetDateTime verifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_user_id")
    private User payerUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_id")
    private Staff verifiedBy;

    @OneToMany(mappedBy = "payment")
    private Set<PaymentAllocation> paymentPaymentAllocations = new HashSet<>();

    @OneToMany(mappedBy = "payment")
    private Set<Receipt> paymentReceipts = new HashSet<>();

}
