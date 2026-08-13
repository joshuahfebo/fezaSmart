package com.fezaschools.fezasmart.permission;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    Permission findFirstByStudentId(Integer id);

    Permission findFirstByIssuedByStaffId(Integer id);

    Permission findFirstByGuardOutStaffId(Integer id);

    Permission findFirstByGuardInStaffId(Integer id);

}
