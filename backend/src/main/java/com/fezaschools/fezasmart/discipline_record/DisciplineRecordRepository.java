package com.fezaschools.fezasmart.discipline_record;

import org.springframework.data.jpa.repository.JpaRepository;


public interface DisciplineRecordRepository extends JpaRepository<DisciplineRecord, Integer> {

    DisciplineRecord findFirstByStudentId(Integer id);

    DisciplineRecord findFirstByViolationId(Integer id);

    DisciplineRecord findFirstByStaffId(Integer id);

}
