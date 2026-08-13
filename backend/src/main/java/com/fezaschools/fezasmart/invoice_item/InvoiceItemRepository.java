package com.fezaschools.fezasmart.invoice_item;

import org.springframework.data.jpa.repository.JpaRepository;


public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Integer> {

    InvoiceItem findFirstByInvoiceId(Integer id);

    InvoiceItem findFirstByFeeItemId(Integer id);

}
