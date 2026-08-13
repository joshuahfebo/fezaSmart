package com.fezaschools.fezasmart.payment;

import com.fezaschools.fezasmart.events.BeforeDeleteInvoice;
import com.fezaschools.fezasmart.events.BeforeDeletePayment;
import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.events.BeforeDeleteStudent;
import com.fezaschools.fezasmart.events.BeforeDeleteUser;
import com.fezaschools.fezasmart.invoice.Invoice;
import com.fezaschools.fezasmart.invoice.InvoiceRepository;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.student.Student;
import com.fezaschools.fezasmart.student.StudentRepository;
import com.fezaschools.fezasmart.user.User;
import com.fezaschools.fezasmart.user.UserRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final ApplicationEventPublisher publisher;

    public PaymentService(final PaymentRepository paymentRepository,
            final InvoiceRepository invoiceRepository, final StudentRepository studentRepository,
            final UserRepository userRepository, final StaffRepository staffRepository,
            final ApplicationEventPublisher publisher) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
        this.publisher = publisher;
    }

    public List<PaymentDTO> findAll() {
        final List<Payment> payments = paymentRepository.findAll(Sort.by("id"));
        return payments.stream()
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .toList();
    }

    public PaymentDTO get(final Integer id) {
        return paymentRepository.findById(id)
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PaymentDTO paymentDTO) {
        final Payment payment = new Payment();
        mapToEntity(paymentDTO, payment);
        return paymentRepository.save(payment).getId();
    }

    public void update(final Integer id, final PaymentDTO paymentDTO) {
        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(paymentDTO, payment);
        paymentRepository.save(payment);
    }

    public void delete(final Integer id) {
        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeletePayment(id));
        paymentRepository.delete(payment);
    }

    private PaymentDTO mapToDTO(final Payment payment, final PaymentDTO paymentDTO) {
        paymentDTO.setId(payment.getId());
        paymentDTO.setPaymentNumber(payment.getPaymentNumber());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setPaymentMethod(payment.getPaymentMethod());
        paymentDTO.setTransactionReference(payment.getTransactionReference());
        paymentDTO.setPaymentDate(payment.getPaymentDate());
        paymentDTO.setStatus(payment.getStatus());
        paymentDTO.setVerifiedAt(payment.getVerifiedAt());
        paymentDTO.setInvoice(payment.getInvoice() == null ? null : payment.getInvoice().getId());
        paymentDTO.setStudent(payment.getStudent() == null ? null : payment.getStudent().getId());
        paymentDTO.setPayerUser(payment.getPayerUser() == null ? null : payment.getPayerUser().getId());
        paymentDTO.setVerifiedBy(payment.getVerifiedBy() == null ? null : payment.getVerifiedBy().getId());
        return paymentDTO;
    }

    private Payment mapToEntity(final PaymentDTO paymentDTO, final Payment payment) {
        payment.setPaymentNumber(paymentDTO.getPaymentNumber());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setTransactionReference(paymentDTO.getTransactionReference());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setStatus(paymentDTO.getStatus());
        payment.setVerifiedAt(paymentDTO.getVerifiedAt());
        final Invoice invoice = paymentDTO.getInvoice() == null ? null : invoiceRepository.findById(paymentDTO.getInvoice())
                .orElseThrow(() -> new NotFoundException("invoice not found"));
        payment.setInvoice(invoice);
        final Student student = paymentDTO.getStudent() == null ? null : studentRepository.findById(paymentDTO.getStudent())
                .orElseThrow(() -> new NotFoundException("student not found"));
        payment.setStudent(student);
        final User payerUser = paymentDTO.getPayerUser() == null ? null : userRepository.findById(paymentDTO.getPayerUser())
                .orElseThrow(() -> new NotFoundException("payerUser not found"));
        payment.setPayerUser(payerUser);
        final Staff verifiedBy = paymentDTO.getVerifiedBy() == null ? null : staffRepository.findById(paymentDTO.getVerifiedBy())
                .orElseThrow(() -> new NotFoundException("verifiedBy not found"));
        payment.setVerifiedBy(verifiedBy);
        return payment;
    }

    @EventListener(BeforeDeleteInvoice.class)
    public void on(final BeforeDeleteInvoice event) {
        final ReferencedException referencedException = new ReferencedException();
        final Payment invoicePayment = paymentRepository.findFirstByInvoiceId(event.getId());
        if (invoicePayment != null) {
            referencedException.setKey("invoice.payment.invoice.referenced");
            referencedException.addParam(invoicePayment.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStudent.class)
    public void on(final BeforeDeleteStudent event) {
        final ReferencedException referencedException = new ReferencedException();
        final Payment studentPayment = paymentRepository.findFirstByStudentId(event.getId());
        if (studentPayment != null) {
            referencedException.setKey("student.payment.student.referenced");
            referencedException.addParam(studentPayment.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final Payment payerUserPayment = paymentRepository.findFirstByPayerUserId(event.getId());
        if (payerUserPayment != null) {
            referencedException.setKey("user.payment.payerUser.referenced");
            referencedException.addParam(payerUserPayment.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final Payment verifiedByPayment = paymentRepository.findFirstByVerifiedById(event.getId());
        if (verifiedByPayment != null) {
            referencedException.setKey("staff.payment.verifiedBy.referenced");
            referencedException.addParam(verifiedByPayment.getId());
            throw referencedException;
        }
    }

}
