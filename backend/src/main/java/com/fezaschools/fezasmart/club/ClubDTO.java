package com.fezaschools.fezasmart.club;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClubDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    private OffsetDateTime createdAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    private Integer school;

    private Integer patronStaff;

}
