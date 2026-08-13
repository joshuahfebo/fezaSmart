package com.fezaschools.fezasmart.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InvoiceDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String invoiceNumber;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal totalAmount;

    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal discountAmount;

    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal paidAmount;

    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal balance;

    @Size(max = 255)
    private String status;

    private LocalDate issuedDate;

    private LocalDate dueDate;

    @NotNull
    private Integer student;

    private Integer feeStructure;

    private Integer academicYear;

    private Integer term;

    private Integer issuedBy;

}
