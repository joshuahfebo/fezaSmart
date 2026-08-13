package com.fezaschools.fezasmart.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String paymentNumber;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal amount;

    @Size(max = 255)
    private String paymentMethod;

    @Size(max = 255)
    private String transactionReference;

    private OffsetDateTime paymentDate;

    @Size(max = 255)
    private String status;

    private OffsetDateTime verifiedAt;

    private Integer invoice;

    @NotNull
    private Integer student;

    private Integer payerUser;

    private Integer verifiedBy;

}
