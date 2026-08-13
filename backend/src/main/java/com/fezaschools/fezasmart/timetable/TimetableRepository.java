package com.fezaschools.fezasmart.timetable;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TimetableRepository extends JpaRepository<Timetable, Integer> {

    Timetable findFirstByAcademicYearId(Integer id);

}
