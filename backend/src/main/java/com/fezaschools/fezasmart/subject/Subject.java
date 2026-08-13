package com.fezaschools.fezasmart.subject;

import com.fezaschools.fezasmart.combination.Combination;
import com.fezaschools.fezasmart.exam_subject.ExamSubject;
import com.fezaschools.fezasmart.fee_item.FeeItem;
import com.fezaschools.fezasmart.grade_boundary.GradeBoundary;
import com.fezaschools.fezasmart.lesson.Lesson;
import com.fezaschools.fezasmart.staff.Staff;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Subject {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @ManyToMany(mappedBy = "teacherSubjectSubjects")
    private Set<Staff> teacherSubjectStaffs = new HashSet<>();

    @ManyToMany(mappedBy = "combinationSubjectSubjects")
    private Set<Combination> combinationSubjectCombinations = new HashSet<>();

    @OneToMany(mappedBy = "subject")
    private Set<Lesson> subjectLessons = new HashSet<>();

    @OneToMany(mappedBy = "subject")
    private Set<ExamSubject> subjectExamSubjects = new HashSet<>();

    @OneToMany(mappedBy = "subject")
    private Set<GradeBoundary> subjectGradeBoundaries = new HashSet<>();

    @OneToMany(mappedBy = "subject")
    private Set<FeeItem> subjectFeeItems = new HashSet<>();

}
