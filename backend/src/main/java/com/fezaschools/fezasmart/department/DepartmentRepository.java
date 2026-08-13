package com.fezaschools.fezasmart.department;

import org.springframework.data.jpa.repository.JpaRepository;


public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Department findFirstBySchoolId(Integer id);

    Department findFirstByHeadStaffId(Integer id);

}
