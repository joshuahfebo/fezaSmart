package com.fezaschools.fezasmart.combination;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CombinationDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    private OffsetDateTime createdAt;

    @NotNull
    private Integer classs;

    private Integer timetable;

    private List<Integer> combinationSubjectSubjects;

}
