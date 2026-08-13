package com.fezaschools.fezasmart.club_member;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ClubMemberRepository extends JpaRepository<ClubMember, Integer> {

    ClubMember findFirstByClubId(Integer id);

    ClubMember findFirstByStudentId(Integer id);

}
