package com.fezaschools.fezasmart.parent;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ParentRepository extends JpaRepository<Parent, Integer> {

    Parent findFirstByUserId(Integer id);

}
