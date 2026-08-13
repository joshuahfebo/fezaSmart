package com.fezaschools.fezasmart.club;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ClubRepository extends JpaRepository<Club, Integer> {

    Club findFirstBySchoolId(Integer id);

    Club findFirstByPatronStaffId(Integer id);

}
