package com.fezaschools.fezasmart.receipt;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReceiptDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String receiptNumber;

    private OffsetDateTime receiptDate;

    private String receiptData;

    @NotNull
    private Integer payment;

    private Integer generatedBy;

}
