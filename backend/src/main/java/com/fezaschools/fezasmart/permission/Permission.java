package com.fezaschools.fezasmart.permission;

import com.fezaschools.fezasmart.leave_request.LeaveRequest;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.student.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Permission {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer leaveRequestId;

    @Column(nullable = false)
    private OffsetDateTime timeOutLimit;

    @Column(nullable = false)
    private OffsetDateTime timeInLimit;

    @Column
    private OffsetDateTime actualTimeOut;

    @Column
    private OffsetDateTime actualTimeIn;

    @Column
    private Boolean returned;

    @OneToMany(mappedBy = "permission")
    private Set<LeaveRequest> leaveRequests = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_by_staff_id")
    private Staff issuedByStaff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guard_out_staff_id")
    private Staff guardOutStaff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guard_in_staff_id")
    private Staff guardInStaff;

}
