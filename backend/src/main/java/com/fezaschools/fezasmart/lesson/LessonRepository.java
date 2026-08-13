package com.fezaschools.fezasmart.lesson;

import org.springframework.data.jpa.repository.JpaRepository;


public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    Lesson findFirstByTimetableId(Integer id);

    Lesson findFirstBySubjectId(Integer id);

    Lesson findFirstByTeacherId(Integer id);

}
