package com.fezaschools.fezasmart.receipt;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

    Receipt findFirstByPaymentId(Integer id);

    Receipt findFirstByGeneratedById(Integer id);

}
