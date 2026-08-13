package com.fezaschools.fezasmart.role;

import com.fezaschools.fezasmart.staff_role.StaffRole;
import com.fezaschools.fezasmart.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "\"role\"")
@Getter
@Setter
public class Role {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "userRoleRoles")
    private Set<User> userRoleUsers = new HashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<StaffRole> roleStaffRoles = new HashSet<>();

}
