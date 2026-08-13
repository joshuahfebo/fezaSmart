package com.fezaschools.fezasmart.staff_role;

import org.springframework.data.jpa.repository.JpaRepository;


public interface StaffRoleRepository extends JpaRepository<StaffRole, Long> {

    StaffRole findFirstByStaffId(Integer id);

    StaffRole findFirstByRoleId(Integer id);

}
