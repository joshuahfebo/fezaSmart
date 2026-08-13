package com.fezaschools.fezasmart.club_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClubMemberDTO {

    private Integer id;

    private LocalDate joinedDate;

    @Size(max = 255)
    private String roleInClub;

    @JsonProperty("isActive")
    private Boolean isActive;

    @NotNull
    private Integer club;

    @NotNull
    private Integer student;

}
