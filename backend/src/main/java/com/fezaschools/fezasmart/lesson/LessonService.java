package com.fezaschools.fezasmart.lesson;

import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.events.BeforeDeleteSubject;
import com.fezaschools.fezasmart.events.BeforeDeleteTimetable;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.subject.Subject;
import com.fezaschools.fezasmart.subject.SubjectRepository;
import com.fezaschools.fezasmart.timetable.Timetable;
import com.fezaschools.fezasmart.timetable.TimetableRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final TimetableRepository timetableRepository;
    private final SubjectRepository subjectRepository;
    private final StaffRepository staffRepository;

    public LessonService(final LessonRepository lessonRepository,
            final TimetableRepository timetableRepository,
            final SubjectRepository subjectRepository, final StaffRepository staffRepository) {
        this.lessonRepository = lessonRepository;
        this.timetableRepository = timetableRepository;
        this.subjectRepository = subjectRepository;
        this.staffRepository = staffRepository;
    }

    public List<LessonDTO> findAll() {
        final List<Lesson> lessons = lessonRepository.findAll(Sort.by("id"));
        return lessons.stream()
                .map(lesson -> mapToDTO(lesson, new LessonDTO()))
                .toList();
    }

    public LessonDTO get(final Integer id) {
        return lessonRepository.findById(id)
                .map(lesson -> mapToDTO(lesson, new LessonDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final LessonDTO lessonDTO) {
        final Lesson lesson = new Lesson();
        mapToEntity(lessonDTO, lesson);
        return lessonRepository.save(lesson).getId();
    }

    public void update(final Integer id, final LessonDTO lessonDTO) {
        final Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(lessonDTO, lesson);
        lessonRepository.save(lesson);
    }

    public void delete(final Integer id) {
        final Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        lessonRepository.delete(lesson);
    }

    private LessonDTO mapToDTO(final Lesson lesson, final LessonDTO lessonDTO) {
        lessonDTO.setId(lesson.getId());
        lessonDTO.setDayOfWeek(lesson.getDayOfWeek());
        lessonDTO.setStartTime(lesson.getStartTime());
        lessonDTO.setEndTime(lesson.getEndTime());
        lessonDTO.setRoom(lesson.getRoom());
        lessonDTO.setTimetable(lesson.getTimetable() == null ? null : lesson.getTimetable().getId());
        lessonDTO.setSubject(lesson.getSubject() == null ? null : lesson.getSubject().getId());
        lessonDTO.setTeacher(lesson.getTeacher() == null ? null : lesson.getTeacher().getId());
        return lessonDTO;
    }

    private Lesson mapToEntity(final LessonDTO lessonDTO, final Lesson lesson) {
        lesson.setDayOfWeek(lessonDTO.getDayOfWeek());
        lesson.setStartTime(lessonDTO.getStartTime());
        lesson.setEndTime(lessonDTO.getEndTime());
        lesson.setRoom(lessonDTO.getRoom());
        final Timetable timetable = lessonDTO.getTimetable() == null ? null : timetableRepository.findById(lessonDTO.getTimetable())
                .orElseThrow(() -> new NotFoundException("timetable not found"));
        lesson.setTimetable(timetable);
        final Subject subject = lessonDTO.getSubject() == null ? null : subjectRepository.findById(lessonDTO.getSubject())
                .orElseThrow(() -> new NotFoundException("subject not found"));
        lesson.setSubject(subject);
        final Staff teacher = lessonDTO.getTeacher() == null ? null : staffRepository.findById(lessonDTO.getTeacher())
                .orElseThrow(() -> new NotFoundException("teacher not found"));
        lesson.setTeacher(teacher);
        return lesson;
    }

    @EventListener(BeforeDeleteTimetable.class)
    public void on(final BeforeDeleteTimetable event) {
        final ReferencedException referencedException = new ReferencedException();
        final Lesson timetableLesson = lessonRepository.findFirstByTimetableId(event.getId());
        if (timetableLesson != null) {
            referencedException.setKey("timetable.lesson.timetable.referenced");
            referencedException.addParam(timetableLesson.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteSubject.class)
    public void on(final BeforeDeleteSubject event) {
        final ReferencedException referencedException = new ReferencedException();
        final Lesson subjectLesson = lessonRepository.findFirstBySubjectId(event.getId());
        if (subjectLesson != null) {
            referencedException.setKey("subject.lesson.subject.referenced");
            referencedException.addParam(subjectLesson.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final Lesson teacherLesson = lessonRepository.findFirstByTeacherId(event.getId());
        if (teacherLesson != null) {
            referencedException.setKey("staff.lesson.teacher.referenced");
            referencedException.addParam(teacherLesson.getId());
            throw referencedException;
        }
    }

}
