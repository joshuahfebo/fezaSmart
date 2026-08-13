package com.fezaschools.fezasmart.receipt;

import com.fezaschools.fezasmart.events.BeforeDeletePayment;
import com.fezaschools.fezasmart.events.BeforeDeleteStaff;
import com.fezaschools.fezasmart.payment.Payment;
import com.fezaschools.fezasmart.payment.PaymentRepository;
import com.fezaschools.fezasmart.staff.Staff;
import com.fezaschools.fezasmart.staff.StaffRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final PaymentRepository paymentRepository;
    private final StaffRepository staffRepository;

    public ReceiptService(final ReceiptRepository receiptRepository,
            final PaymentRepository paymentRepository, final StaffRepository staffRepository) {
        this.receiptRepository = receiptRepository;
        this.paymentRepository = paymentRepository;
        this.staffRepository = staffRepository;
    }

    public List<ReceiptDTO> findAll() {
        final List<Receipt> receipts = receiptRepository.findAll(Sort.by("id"));
        return receipts.stream()
                .map(receipt -> mapToDTO(receipt, new ReceiptDTO()))
                .toList();
    }

    public ReceiptDTO get(final Integer id) {
        return receiptRepository.findById(id)
                .map(receipt -> mapToDTO(receipt, new ReceiptDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ReceiptDTO receiptDTO) {
        final Receipt receipt = new Receipt();
        mapToEntity(receiptDTO, receipt);
        return receiptRepository.save(receipt).getId();
    }

    public void update(final Integer id, final ReceiptDTO receiptDTO) {
        final Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(receiptDTO, receipt);
        receiptRepository.save(receipt);
    }

    public void delete(final Integer id) {
        final Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        receiptRepository.delete(receipt);
    }

    private ReceiptDTO mapToDTO(final Receipt receipt, final ReceiptDTO receiptDTO) {
        receiptDTO.setId(receipt.getId());
        receiptDTO.setReceiptNumber(receipt.getReceiptNumber());
        receiptDTO.setReceiptDate(receipt.getReceiptDate());
        receiptDTO.setReceiptData(receipt.getReceiptData());
        receiptDTO.setPayment(receipt.getPayment() == null ? null : receipt.getPayment().getId());
        receiptDTO.setGeneratedBy(receipt.getGeneratedBy() == null ? null : receipt.getGeneratedBy().getId());
        return receiptDTO;
    }

    private Receipt mapToEntity(final ReceiptDTO receiptDTO, final Receipt receipt) {
        receipt.setReceiptNumber(receiptDTO.getReceiptNumber());
        receipt.setReceiptDate(receiptDTO.getReceiptDate());
        receipt.setReceiptData(receiptDTO.getReceiptData());
        final Payment payment = receiptDTO.getPayment() == null ? null : paymentRepository.findById(receiptDTO.getPayment())
                .orElseThrow(() -> new NotFoundException("payment not found"));
        receipt.setPayment(payment);
        final Staff generatedBy = receiptDTO.getGeneratedBy() == null ? null : staffRepository.findById(receiptDTO.getGeneratedBy())
                .orElseThrow(() -> new NotFoundException("generatedBy not found"));
        receipt.setGeneratedBy(generatedBy);
        return receipt;
    }

    @EventListener(BeforeDeletePayment.class)
    public void on(final BeforeDeletePayment event) {
        final ReferencedException referencedException = new ReferencedException();
        final Receipt paymentReceipt = receiptRepository.findFirstByPaymentId(event.getId());
        if (paymentReceipt != null) {
            referencedException.setKey("payment.receipt.payment.referenced");
            referencedException.addParam(paymentReceipt.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteStaff.class)
    public void on(final BeforeDeleteStaff event) {
        final ReferencedException referencedException = new ReferencedException();
        final Receipt generatedByReceipt = receiptRepository.findFirstByGeneratedById(event.getId());
        if (generatedByReceipt != null) {
            referencedException.setKey("staff.receipt.generatedBy.referenced");
            referencedException.addParam(generatedByReceipt.getId());
            throw referencedException;
        }
    }

}
