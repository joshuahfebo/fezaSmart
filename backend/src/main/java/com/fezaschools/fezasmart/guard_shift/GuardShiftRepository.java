package com.fezaschools.fezasmart.guard_shift;

import org.springframework.data.jpa.repository.JpaRepository;


public interface GuardShiftRepository extends JpaRepository<GuardShift, Integer> {

    GuardShift findFirstByStaffId(Integer id);

}
