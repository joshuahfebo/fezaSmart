package com.fezaschools.fezasmart.user;

import com.fezaschools.fezasmart.audit_log.AuditLog;
import com.fezaschools.fezasmart.email_verification_token.EmailVerificationToken;
import com.fezaschools.fezasmart.leave_request.LeaveRequest;
import com.fezaschools.fezasmart.login_attempt.LoginAttempt;
import com.fezaschools.fezasmart.notification.Notification;
import com.fezaschools.fezasmart.parent.Parent;
import com.fezaschools.fezasmart.password_reset_token.PasswordResetToken;
import com.fezaschools.fezasmart.payment.Payment;
import com.fezaschools.fezasmart.role.Role;
import com.fezaschools.fezasmart.session.Session;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.two_factor_code.TwoFactorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String hashedPassword;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private Boolean isActive;

    @Column
    private Boolean emailVerified;

    @Column
    private Boolean phoneVerified;

    @Column
    private Boolean twoFactorEnabled;

    @Column
    private String twoFactorMethod;

    @Column
    private String twoFactorSecret;

    @Column
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime updatedAt;

    @Column
    private OffsetDateTime lastLoginAt;

    @Column
    private OffsetDateTime deletedAt;

    @Column
    private Integer deletedBy;

    @Column
    private String restoreToken;

    @OneToMany(mappedBy = "user")
    private Set<Staff> userStaffs = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Student> userStudents = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Parent> userParents = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "UserRole",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Role> userRoleRoles = new HashSet<>();

    @OneToMany(mappedBy = "requesterUser")
    private Set<LeaveRequest> requesterUserLeaveRequests = new HashSet<>();

    @OneToMany(mappedBy = "payerUser")
    private Set<Payment> payerUserPayments = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<PasswordResetToken> userPasswordResetTokens = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<EmailVerificationToken> userEmailVerificationTokens = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<TwoFactorCode> userTwoFactorCodes = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<LoginAttempt> userLoginAttempts = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Session> userSessions = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<AuditLog> userAuditLogs = new HashSet<>();

    @OneToMany(mappedBy = "recipientUser")
    private Set<Notification> recipientUserNotifications = new HashSet<>();

}
