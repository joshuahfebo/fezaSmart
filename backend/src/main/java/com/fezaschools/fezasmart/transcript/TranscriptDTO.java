package com.fezaschools.fezasmart.transcript;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TranscriptDTO {

    private Integer id;

    private String transcriptData;

    private OffsetDateTime createdAt;

    @NotNull
    private Integer student;

    @NotNull
    private Integer academicYear;

    private Integer generatedBy;

}
