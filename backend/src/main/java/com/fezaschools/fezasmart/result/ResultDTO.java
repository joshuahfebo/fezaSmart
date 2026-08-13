package com.fezaschools.fezasmart.result;

import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResultDTO {

    private Integer id;

    private BigDecimal totalScore;

    private BigDecimal averagePercentage;

    private BigDecimal totalPoints;

    @Size(max = 255)
    private String division;

    private Integer rankInClass;

    @Size(max = 65535)
    private String remark;

    private OffsetDateTime computedAt;

    private Integer student;

    private Integer exam;

}
