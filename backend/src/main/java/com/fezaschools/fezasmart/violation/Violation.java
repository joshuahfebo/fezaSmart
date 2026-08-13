package com.fezaschools.fezasmart.violation;

import com.fezaschools.fezasmart.discipline_record.DisciplineRecord;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Violation {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "longtext", name = "\"description\"")
    private String description;

    @Column(nullable = false, precision = 5, scale = 1)
    private BigDecimal pointDeduction;

    @Column(nullable = false)
    private String pointType;

    @OneToMany(mappedBy = "violation")
    private Set<DisciplineRecord> violationDisciplineRecords = new HashSet<>();

}
