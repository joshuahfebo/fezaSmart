package com.fezaschools.fezasmart.classs;

import com.fezaschools.fezasmart.academic_year.AcademicYear;
import com.fezaschools.fezasmart.attendance_record.AttendanceRecord;
import com.fezaschools.fezasmart.class_assignment.ClassAssignment;
import com.fezaschools.fezasmart.combination.Combination;
import com.fezaschools.fezasmart.fee_item.FeeItem;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.student_enrollment.StudentEnrollment;
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
public class Classs {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @OneToMany(mappedBy = "classs")
    private Set<ClassAssignment> classClassAssignments = new HashSet<>();

    @OneToMany(mappedBy = "classs")
    private Set<StudentEnrollment> classStudentEnrollments = new HashSet<>();

    @OneToMany(mappedBy = "classs")
    private Set<Combination> classCombinations = new HashSet<>();

    @OneToMany(mappedBy = "classs")
    private Set<AttendanceRecord> classAttendanceRecords = new HashSet<>();

    @OneToMany(mappedBy = "classs")
    private Set<FeeItem> classFeeItems = new HashSet<>();

}
