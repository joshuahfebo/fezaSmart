package com.fezaschools.fezasmart.invoice_item;

import com.fezaschools.fezasmart.events.BeforeDeleteFeeItem;
import com.fezaschools.fezasmart.events.BeforeDeleteInvoice;
import com.fezaschools.fezasmart.fee_item.FeeItem;
import com.fezaschools.fezasmart.fee_item.FeeItemRepository;
import com.fezaschools.fezasmart.invoice.Invoice;
import com.fezaschools.fezasmart.invoice.InvoiceRepository;
import com.fezaschools.fezasmart.util.NotFoundException;
import com.fezaschools.fezasmart.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class InvoiceItemService {

    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceRepository invoiceRepository;
    private final FeeItemRepository feeItemRepository;

    public InvoiceItemService(final InvoiceItemRepository invoiceItemRepository,
            final InvoiceRepository invoiceRepository, final FeeItemRepository feeItemRepository) {
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceRepository = invoiceRepository;
        this.feeItemRepository = feeItemRepository;
    }

    public List<InvoiceItemDTO> findAll() {
        final List<InvoiceItem> invoiceItems = invoiceItemRepository.findAll(Sort.by("id"));
        return invoiceItems.stream()
                .map(invoiceItem -> mapToDTO(invoiceItem, new InvoiceItemDTO()))
                .toList();
    }

    public InvoiceItemDTO get(final Integer id) {
        return invoiceItemRepository.findById(id)
                .map(invoiceItem -> mapToDTO(invoiceItem, new InvoiceItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final InvoiceItemDTO invoiceItemDTO) {
        final InvoiceItem invoiceItem = new InvoiceItem();
        mapToEntity(invoiceItemDTO, invoiceItem);
        return invoiceItemRepository.save(invoiceItem).getId();
    }

    public void update(final Integer id, final InvoiceItemDTO invoiceItemDTO) {
        final InvoiceItem invoiceItem = invoiceItemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(invoiceItemDTO, invoiceItem);
        invoiceItemRepository.save(invoiceItem);
    }

    public void delete(final Integer id) {
        final InvoiceItem invoiceItem = invoiceItemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        invoiceItemRepository.delete(invoiceItem);
    }

    private InvoiceItemDTO mapToDTO(final InvoiceItem invoiceItem,
            final InvoiceItemDTO invoiceItemDTO) {
        invoiceItemDTO.setId(invoiceItem.getId());
        invoiceItemDTO.setDescription(invoiceItem.getDescription());
        invoiceItemDTO.setQuantity(invoiceItem.getQuantity());
        invoiceItemDTO.setUnitPrice(invoiceItem.getUnitPrice());
        invoiceItemDTO.setTotal(invoiceItem.getTotal());
        invoiceItemDTO.setInvoice(invoiceItem.getInvoice() == null ? null : invoiceItem.getInvoice().getId());
        invoiceItemDTO.setFeeItem(invoiceItem.getFeeItem() == null ? null : invoiceItem.getFeeItem().getId());
        return invoiceItemDTO;
    }

    private InvoiceItem mapToEntity(final InvoiceItemDTO invoiceItemDTO,
            final InvoiceItem invoiceItem) {
        invoiceItem.setDescription(invoiceItemDTO.getDescription());
        invoiceItem.setQuantity(invoiceItemDTO.getQuantity());
        invoiceItem.setUnitPrice(invoiceItemDTO.getUnitPrice());
        invoiceItem.setTotal(invoiceItemDTO.getTotal());
        final Invoice invoice = invoiceItemDTO.getInvoice() == null ? null : invoiceRepository.findById(invoiceItemDTO.getInvoice())
                .orElseThrow(() -> new NotFoundException("invoice not found"));
        invoiceItem.setInvoice(invoice);
        final FeeItem feeItem = invoiceItemDTO.getFeeItem() == null ? null : feeItemRepository.findById(invoiceItemDTO.getFeeItem())
                .orElseThrow(() -> new NotFoundException("feeItem not found"));
        invoiceItem.setFeeItem(feeItem);
        return invoiceItem;
    }

    @EventListener(BeforeDeleteInvoice.class)
    public void on(final BeforeDeleteInvoice event) {
        final ReferencedException referencedException = new ReferencedException();
        final InvoiceItem invoiceInvoiceItem = invoiceItemRepository.findFirstByInvoiceId(event.getId());
        if (invoiceInvoiceItem != null) {
            referencedException.setKey("invoice.invoiceItem.invoice.referenced");
            referencedException.addParam(invoiceInvoiceItem.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteFeeItem.class)
    public void on(final BeforeDeleteFeeItem event) {
        final ReferencedException referencedException = new ReferencedException();
        final InvoiceItem feeItemInvoiceItem = invoiceItemRepository.findFirstByFeeItemId(event.getId());
        if (feeItemInvoiceItem != null) {
            referencedException.setKey("feeItem.invoiceItem.feeItem.referenced");
            referencedException.addParam(feeItemInvoiceItem.getId());
            throw referencedException;
        }
    }

}
