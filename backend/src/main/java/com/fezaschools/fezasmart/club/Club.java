package com.fezaschools.fezasmart.club;

import com.fezaschools.fezasmart.club_member.ClubMember;
import com.fezaschools.fezasmart.school.School;
import com.fezaschools.fezasmart.staff.Staff;
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
public class Club {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "longtext", name = "\"description\"")
    private String description;

    @Column
    private OffsetDateTime createdAt;

    @Column
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patron_staff_id")
    private Staff patronStaff;

    @OneToMany(mappedBy = "club")
    private Set<ClubMember> clubClubMembers = new HashSet<>();

}
