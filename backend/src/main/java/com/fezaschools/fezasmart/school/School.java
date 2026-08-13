package com.fezaschools.fezasmart.school;

import com.fezaschools.fezasmart.api_key.ApiKey;
import com.fezaschools.fezasmart.classs.Classs;
import com.fezaschools.fezasmart.club.Club;
import com.fezaschools.fezasmart.department.Department;
import com.fezaschools.fezasmart.fee_structure.FeeStructure;
import com.fezaschools.fezasmart.grade_boundary.GradeBoundary;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.student.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class School {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private String location;

    @Column
    private Boolean isActive;

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

    @OneToMany(mappedBy = "school")
    private Set<Staff> schoolStaffs = new HashSet<>();

    @OneToMany(mappedBy = "school")
    private Set<Student> schoolStudents = new HashSet<>();

    @OneToMany(mappedBy = "school")
    private Set<FeeStructure> schoolFeeStructures = new HashSet<>();

    @OneToMany(mappedBy = "school")
    private Set<Club> schoolClubs = new HashSet<>();

    @OneToMany(mappedBy = "school")
    private Set<Department> schoolDepartments = new HashSet<>();

    @OneToMany(mappedBy = "school")
    private Set<Classs> schoolClasses = new HashSet<>();

    @OneToMany(mappedBy = "school")
    private Set<GradeBoundary> schoolGradeBoundaries = new HashSet<>();

    @OneToMany(mappedBy = "school")
    private Set<ApiKey> schoolApiKeys = new HashSet<>();

}
