package com.fezaschools.fezasmart.combination;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CombinationRepository extends JpaRepository<Combination, Integer> {

    Combination findFirstByClasssId(Integer id);

    Combination findFirstByTimetableId(Integer id);

    List<Combination> findAllByCombinationSubjectSubjectsId(Integer id);

}
