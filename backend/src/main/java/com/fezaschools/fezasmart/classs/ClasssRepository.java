package com.fezaschools.fezasmart.classs;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ClasssRepository extends JpaRepository<Classs, Integer> {

    Classs findFirstBySchoolId(Integer id);

    Classs findFirstByAcademicYearId(Integer id);

}
