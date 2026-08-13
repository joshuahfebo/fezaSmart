package com.fezaschools.fezasmart.invoice;

import com.fezaschools.fezasmart.academic_year.AcademicYear;
import com.fezaschools.fezasmart.academic_year.AcademicYearRepository;
import com.fezaschools.fezasmart.events.BeforeDeleteAcademicYear;
import com.fezaschools.fezasmart.events.BeforeDeleteFeeStructure;
import com.fezaschools.fezasmart.events.BeforeDeleteInvoice;
import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.events.BeforeDeleteStudent;
import com.fezaschools.fezasmart.events.BeforeDeleteTerm;
import com.fezaschools.fezasmart.fee_structure.FeeStructure;
import com.fezaschools.fezasmart.fee_structure.FeeStructureRepository;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.student.StudentRepository;
import com.fezaschools.fezasmart.term.Term;
import com.fezaschools.fezasmart.term.TermRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final StudentRepository studentRepository;
    private final FeeStructureRepository feeStructureRepository;
    private final AcademicYearRepository academicYearRepository;
    private final TermRepository termRepository;
    private final StaffRepository staffRepository;
    private final ApplicationEventPublisher publisher;

    public InvoiceService(final InvoiceRepository invoiceRepository,
            final StudentRepository studentRepository,
            final FeeStructureRepository feeStructureRepository,
            final AcademicYearRepository academicYearRepository,
            final TermRepository termRepository, final StaffRepository staffRepository,
            final ApplicationEventPublisher publisher) {
        this.invoiceRepository = invoiceRepository;
        this.studentRepository = studentRepository;
        this.feeStructureRepository = feeStructureRepository;
        this.academicYearRepository = academicYearRepository;
        this.termRepository = termRepository;
        this.staffRepository = staffRepository;
        this.publisher = publisher;
    }

    public List<InvoiceDTO> findAll() {
        final List<Invoice> invoices = invoiceRepository.findAll(Sort.by("id"));
        return invoices.stream()
                .map(invoice -> mapToDTO(invoice, new InvoiceDTO()))
                .toList();
    }

    public InvoiceDTO get(final Integer id) {
        return invoiceRepository.findById(id)
                .map(invoice -> mapToDTO(invoice, new InvoiceDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final InvoiceDTO invoiceDTO) {
        final Invoice invoice = new Invoice();
        mapToEntity(invoiceDTO, invoice);
        return invoiceRepository.save(invoice).getId();
    }

    public void update(final Integer id, final InvoiceDTO invoiceDTO) {
        final Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(invoiceDTO, invoice);
        invoiceRepository.save(invoice);
    }

    public void delete(final Integer id) {
        final Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteInvoice(id));
        invoiceRepository.delete(invoice);
    }

    private InvoiceDTO mapToDTO(final Invoice invoice, final InvoiceDTO invoiceDTO) {
        invoiceDTO.setId(invoice.getId());
        invoiceDTO.setInvoiceNumber(invoice.getInvoiceNumber());
        invoiceDTO.setTotalAmount(invoice.getTotalAmount());
        invoiceDTO.setDiscountAmount(invoice.getDiscountAmount());
        invoiceDTO.setPaidAmount(invoice.getPaidAmount());
        invoiceDTO.setBalance(invoice.getBalance());
        invoiceDTO.setStatus(invoice.getStatus());
        invoiceDTO.setIssuedDate(invoice.getIssuedDate());
        invoiceDTO.setDueDate(invoice.getDueDate());
        invoiceDTO.setStudent(invoice.getStudent() == null ? null : invoice.getStudent().getId());
        invoiceDTO.setFeeStructure(invoice.getFeeStructure() == null ? null : invoice.getFeeStructure().getId());
        invoiceDTO.setAcademicYear(invoice.getAcademicYear() == null ? null : invoice.getAcademicYear().getId());
        invoiceDTO.setTerm(invoice.getTerm() == null ? null : invoice.getTerm().getId());
        invoiceDTO.setIssuedBy(invoice.getIssuedBy() == null ? null : invoice.getIssuedBy().getId());
        return invoiceDTO;
    }

    private Invoice mapToEntity(final InvoiceDTO invoiceDTO, final Invoice invoice) {
        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setTotalAmount(invoiceDTO.getTotalAmount());
        invoice.setDiscountAmount(invoiceDTO.getDiscountAmount());
        invoice.setPaidAmount(invoiceDTO.getPaidAmount());
        invoice.setBalance(invoiceDTO.getBalance());
        invoice.setStatus(invoiceDTO.getStatus());
        invoice.setIssuedDate(invoiceDTO.getIssuedDate());
        invoice.setDueDate(invoiceDTO.getDueDate());
        final Student student = invoiceDTO.getStudent() == null ? null : studentRepository.findById(invoiceDTO.getStudent())
                .orElseThrow(() -> new NotFoundException("student not found"));
        invoice.setStudent(student);
        final FeeStructure feeStructure = invoiceDTO.getFeeStructure() == null ? null : feeStructureRepository.findById(invoiceDTO.getFeeStructure())
                .orElseThrow(() -> new NotFoundException("feeStructure not found"));
        invoice.setFeeStructure(feeStructure);
        final AcademicYear academicYear = invoiceDTO.getAcademicYear() == null ? null : academicYearRepository.findById(invoiceDTO.getAcademicYear())
                .orElseThrow(() -> new NotFoundException("academicYear not found"));
        invoice.setAcademicYear(academicYear);
        final Term term = invoiceDTO.getTerm() == null ? null : termRepository.findById(invoiceDTO.getTerm())
                .orElseThrow(() -> new NotFoundException("term not found"));
        invoice.setTerm(term);
        final Staff issuedBy = invoiceDTO.getIssuedBy() == null ? null : staffRepository.findById(invoiceDTO.getIssuedBy())
                .orElseThrow(() -> new NotFoundException("issuedBy not found"));
        invoice.setIssuedBy(issuedBy);
        return invoice;
    }

    @EventListener(BeforeDeleteStudent.class)
    public void on(final BeforeDeleteStudent event) {
        final ReferencedException referencedException = new ReferencedException();
        final Invoice studentInvoice = invoiceRepository.findFirstByStudentId(event.getId());
        if (studentInvoice != null) {
            referencedException.setKey("student.invoice.student.referenced");
            referencedException.addParam(studentInvoice.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteFeeStructure.class)
    public void on(final BeforeDeleteFeeStructure event) {
        final ReferencedException referencedException = new ReferencedException();
        final Invoice feeStructureInvoice = invoiceRepository.findFirstByFeeStructureId(event.getId());
        if (feeStructureInvoice != null) {
            referencedException.setKey("feeStructure.invoice.feeStructure.referenced");
            referencedException.addParam(feeStructureInvoice.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteAcademicYear.class)
    public void on(final BeforeDeleteAcademicYear event) {
        final ReferencedException referencedException = new ReferencedException();
        final Invoice academicYearInvoice = invoiceRepository.findFirstByAcademicYearId(event.getId());
        if (academicYearInvoice != null) {
            referencedException.setKey("academicYear.invoice.academicYear.referenced");
            referencedException.addParam(academicYearInvoice.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteTerm.class)
    public void on(final BeforeDeleteTerm event) {
        final ReferencedException referencedException = new ReferencedException();
        final Invoice termInvoice = invoiceRepository.findFirstByTermId(event.getId());
        if (termInvoice != null) {
            referencedException.setKey("term.invoice.term.referenced");
            referencedException.addParam(termInvoice.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final Invoice issuedByInvoice = invoiceRepository.findFirstByIssuedById(event.getId());
        if (issuedByInvoice != null) {
            referencedException.setKey("staff.invoice.issuedBy.referenced");
            referencedException.addParam(issuedByInvoice.getId());
            throw referencedException;
        }
    }

}
