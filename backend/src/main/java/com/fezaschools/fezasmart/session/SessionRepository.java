package com.fezaschools.fezasmart.session;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SessionRepository extends JpaRepository<Session, Integer> {

    Session findFirstByUserId(Integer id);

}
