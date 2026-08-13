package com.fezaschools.fezasmart.student;

import com.fezaschools.fezasmart.attendance_record.AttendanceRecord;
import com.fezaschools.fezasmart.club_member.ClubMember;
import com.fezaschools.fezasmart.discipline_record.DisciplineRecord;
import com.fezaschools.fezasmart.invoice.Invoice;
import com.fezaschools.fezasmart.leave_request.LeaveRequest;
import com.fezaschools.fezasmart.parent.Parent;
import com.fezaschools.fezasmart.payment.Payment;
import com.fezaschools.fezasmart.permission.Permission;
import com.fezaschools.fezasmart.result.Result;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.student_enrollment.StudentEnrollment;
import com.fezaschools.fezasmart.student_fee_assignment.StudentFeeAssignment;
import com.fezaschools.fezasmart.student_point.StudentPoint;
import com.fezaschools.fezasmart.student_scholarship.StudentScholarship;
import com.fezaschools.fezasmart.student_score.StudentScore;
import com.fezaschools.fezasmart.transcript.Transcript;
import com.fezaschools.fezasmart.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Student {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String controlNumber;

    @Column
    private LocalDate dob;

    @Column
    private String gender;

    @Column
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime updatedAt;

    @Column
    private OffsetDateTime deletedAt;

    @Column
    private Integer deletedBy;

    @Column
    private String restoreToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "student")
    private Set<StudentEnrollment> studentStudentEnrollments = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "StudentParent",
            joinColumns = @JoinColumn(name = "studentId"),
            inverseJoinColumns = @JoinColumn(name = "parentId")
    )
    private Set<Parent> studentParentParents = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<AttendanceRecord> studentAttendanceRecords = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<LeaveRequest> studentLeaveRequests = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Permission> studentPermissions = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<StudentScore> studentStudentScores = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Result> studentResults = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Transcript> studentTranscripts = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<StudentFeeAssignment> studentStudentFeeAssignments = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<StudentScholarship> studentStudentScholarships = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Invoice> studentInvoices = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Payment> studentPayments = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<ClubMember> studentClubMembers = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<StudentPoint> studentStudentPoints = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<DisciplineRecord> studentDisciplineRecords = new HashSet<>();

}
