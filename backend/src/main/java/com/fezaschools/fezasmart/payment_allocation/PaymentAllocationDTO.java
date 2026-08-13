package com.fezaschools.fezasmart.payment_allocation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentAllocationDTO {

    private Integer id;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal amountAllocated;

    @NotNull
    private Integer payment;

    @NotNull
    private Integer invoice;

}
