package com.fezaschools.fezasmart.payment_allocation;

import com.fezaschools.fezasmart.events.BeforeDeleteInvoice;
import com.fezaschools.fezasmart.events.BeforeDeletePayment;
import com.fezaschools.fezasmart.invoice.Invoice;
import com.fezaschools.fezasmart.invoice.InvoiceRepository;
import com.fezaschools.fezasmart.payment.Payment;
import com.fezaschools.fezasmart.payment.PaymentRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PaymentAllocationService {

    private final PaymentAllocationRepository paymentAllocationRepository;
    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public PaymentAllocationService(final PaymentAllocationRepository paymentAllocationRepository,
            final PaymentRepository paymentRepository, final InvoiceRepository invoiceRepository) {
        this.paymentAllocationRepository = paymentAllocationRepository;
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public List<PaymentAllocationDTO> findAll() {
        final List<PaymentAllocation> paymentAllocations = paymentAllocationRepository.findAll(Sort.by("id"));
        return paymentAllocations.stream()
                .map(paymentAllocation -> mapToDTO(paymentAllocation, new PaymentAllocationDTO()))
                .toList();
    }

    public PaymentAllocationDTO get(final Integer id) {
        return paymentAllocationRepository.findById(id)
                .map(paymentAllocation -> mapToDTO(paymentAllocation, new PaymentAllocationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PaymentAllocationDTO paymentAllocationDTO) {
        final PaymentAllocation paymentAllocation = new PaymentAllocation();
        mapToEntity(paymentAllocationDTO, paymentAllocation);
        return paymentAllocationRepository.save(paymentAllocation).getId();
    }

    public void update(final Integer id, final PaymentAllocationDTO paymentAllocationDTO) {
        final PaymentAllocation paymentAllocation = paymentAllocationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(paymentAllocationDTO, paymentAllocation);
        paymentAllocationRepository.save(paymentAllocation);
    }

    public void delete(final Integer id) {
        final PaymentAllocation paymentAllocation = paymentAllocationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        paymentAllocationRepository.delete(paymentAllocation);
    }

    private PaymentAllocationDTO mapToDTO(final PaymentAllocation paymentAllocation,
            final PaymentAllocationDTO paymentAllocationDTO) {
        paymentAllocationDTO.setId(paymentAllocation.getId());
        paymentAllocationDTO.setAmountAllocated(paymentAllocation.getAmountAllocated());
        paymentAllocationDTO.setPayment(paymentAllocation.getPayment() == null ? null : paymentAllocation.getPayment().getId());
        paymentAllocationDTO.setInvoice(paymentAllocation.getInvoice() == null ? null : paymentAllocation.getInvoice().getId());
        return paymentAllocationDTO;
    }

    private PaymentAllocation mapToEntity(final PaymentAllocationDTO paymentAllocationDTO,
            final PaymentAllocation paymentAllocation) {
        paymentAllocation.setAmountAllocated(paymentAllocationDTO.getAmountAllocated());
        final Payment payment = paymentAllocationDTO.getPayment() == null ? null : paymentRepository.findById(paymentAllocationDTO.getPayment())
                .orElseThrow(() -> new NotFoundException("payment not found"));
        paymentAllocation.setPayment(payment);
        final Invoice invoice = paymentAllocationDTO.getInvoice() == null ? null : invoiceRepository.findById(paymentAllocationDTO.getInvoice())
                .orElseThrow(() -> new NotFoundException("invoice not found"));
        paymentAllocation.setInvoice(invoice);
        return paymentAllocation;
    }

    @EventListener(BeforeDeletePayment.class)
    public void on(final BeforeDeletePayment event) {
        final ReferencedException referencedException = new ReferencedException();
        final PaymentAllocation paymentPaymentAllocation = paymentAllocationRepository.findFirstByPaymentId(event.getId());
        if (paymentPaymentAllocation != null) {
            referencedException.setKey("payment.paymentAllocation.payment.referenced");
            referencedException.addParam(paymentPaymentAllocation.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteInvoice.class)
    public void on(final BeforeDeleteInvoice event) {
        final ReferencedException referencedException = new ReferencedException();
        final PaymentAllocation invoicePaymentAllocation = paymentAllocationRepository.findFirstByInvoiceId(event.getId());
        if (invoicePaymentAllocation != null) {
            referencedException.setKey("invoice.paymentAllocation.invoice.referenced");
            referencedException.addParam(invoicePaymentAllocation.getId());
            throw referencedException;
        }
    }

}
