package com.fezaschools.fezasmart.invoice;

import org.springframework.data.jpa.repository.JpaRepository;


public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    Invoice findFirstByStudentId(Integer id);

    Invoice findFirstByFeeStructureId(Integer id);

    Invoice findFirstByAcademicYearId(Integer id);

    Invoice findFirstByTermId(Integer id);

    Invoice findFirstByIssuedById(Integer id);

}
