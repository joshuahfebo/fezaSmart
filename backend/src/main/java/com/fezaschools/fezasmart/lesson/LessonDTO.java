package com.fezaschools.fezasmart.lesson;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LessonDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String dayOfWeek;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @Size(max = 255)
    private String room;

    @NotNull
    private Integer timetable;

    @NotNull
    private Integer subject;

    @NotNull
    private Integer teacher;

}
