package com.fezaschools.fezasmart.payment_allocation;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentAllocationRepository extends JpaRepository<PaymentAllocation, Integer> {

    PaymentAllocation findFirstByPaymentId(Integer id);

    PaymentAllocation findFirstByInvoiceId(Integer id);

}
