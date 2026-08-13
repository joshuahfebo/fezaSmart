package com.fezaschools.fezasmart.staff;

import com.fezaschools.fezasmart.attendance_record.AttendanceRecord;
import com.fezaschools.fezasmart.class_assignment.ClassAssignment;
import com.fezaschools.fezasmart.club.Club;
import com.fezaschools.fezasmart.department.Department;
import com.fezaschools.fezasmart.discipline_record.DisciplineRecord;
import com.fezaschools.fezasmart.guard_shift.GuardShift;
import com.fezaschools.fezasmart.invoice.Invoice;
import com.fezaschools.fezasmart.leave_request.LeaveRequest;
import com.fezaschools.fezasmart.lesson.Lesson;
import com.fezaschools.fezasmart.payment.Payment;
import com.fezaschools.fezasmart.permission.Permission;
import com.fezaschools.fezasmart.receipt.Receipt;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.staff_role.StaffRole;
import com.fezaschools.fezasmart.student_fee_assignment.StudentFeeAssignment;
import com.fezaschools.fezasmart.student_scholarship.StudentScholarship;
import com.fezaschools.fezasmart.subject.Subject;
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
public class Staff {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private LocalDate dob;

    @Column
    private String gender;

    @Column
    private String staffNumber;

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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "headStaff")
    private Set<Department> headStaffDepartments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "staff")
    private Set<StaffRole> staffStaffRoles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "TeacherSubject",
            joinColumns = @JoinColumn(name = "staffId"),
            inverseJoinColumns = @JoinColumn(name = "subjectId")
    )
    private Set<Subject> teacherSubjectSubjects = new HashSet<>();

    @OneToMany(mappedBy = "staff")
    private Set<ClassAssignment> staffClassAssignments = new HashSet<>();

    @OneToMany(mappedBy = "teacher")
    private Set<Lesson> teacherLessons = new HashSet<>();

    @OneToMany(mappedBy = "markedByStaff")
    private Set<AttendanceRecord> markedByStaffAttendanceRecords = new HashSet<>();

    @OneToMany(mappedBy = "processedByStaff")
    private Set<LeaveRequest> processedByStaffLeaveRequests = new HashSet<>();

    @OneToMany(mappedBy = "issuedByStaff")
    private Set<Permission> issuedByStaffPermissions = new HashSet<>();

    @OneToMany(mappedBy = "guardOutStaff")
    private Set<Permission> guardOutStaffPermissions = new HashSet<>();

    @OneToMany(mappedBy = "guardInStaff")
    private Set<Permission> guardInStaffPermissions = new HashSet<>();

    @OneToMany(mappedBy = "staff")
    private Set<GuardShift> staffGuardShifts = new HashSet<>();

    @OneToMany(mappedBy = "generatedBy")
    private Set<Transcript> generatedByTranscripts = new HashSet<>();

    @OneToMany(mappedBy = "assignedBy")
    private Set<StudentFeeAssignment> assignedByStudentFeeAssignments = new HashSet<>();

    @OneToMany(mappedBy = "awardedBy")
    private Set<StudentScholarship> awardedByStudentScholarships = new HashSet<>();

    @OneToMany(mappedBy = "issuedBy")
    private Set<Invoice> issuedByInvoices = new HashSet<>();

    @OneToMany(mappedBy = "verifiedBy")
    private Set<Payment> verifiedByPayments = new HashSet<>();

    @OneToMany(mappedBy = "generatedBy")
    private Set<Receipt> generatedByReceipts = new HashSet<>();

    @OneToMany(mappedBy = "patronStaff")
    private Set<Club> patronStaffClubs = new HashSet<>();

    @OneToMany(mappedBy = "staff")
    private Set<DisciplineRecord> staffDisciplineRecords = new HashSet<>();

}
