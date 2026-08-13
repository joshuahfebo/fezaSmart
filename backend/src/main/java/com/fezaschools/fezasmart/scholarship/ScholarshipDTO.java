package com.fezaschools.fezasmart.scholarship;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ScholarshipDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    @JsonProperty("isActive")
    private Boolean isActive;

    private Integer discount;

}
