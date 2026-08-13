package com.fezaschools.fezasmart.combination;

import com.fezaschools.fezasmart.classs.Classs;
import com.fezaschools.fezasmart.subject.Subject;
import com.fezaschools.fezasmart.timetable.Timetable;
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
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Combination {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classs_id", nullable = false)
    private Classs classs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;

    @ManyToMany
    @JoinTable(
            name = "CombinationSubject",
            joinColumns = @JoinColumn(name = "combinationId"),
            inverseJoinColumns = @JoinColumn(name = "subjectId")
    )
    private Set<Subject> combinationSubjectSubjects = new HashSet<>();

}
