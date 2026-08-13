package com.fezaschools.fezasmart.payment;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Payment findFirstByInvoiceId(Integer id);

    Payment findFirstByStudentId(Integer id);

    Payment findFirstByPayerUserId(Integer id);

    Payment findFirstByVerifiedById(Integer id);

}
