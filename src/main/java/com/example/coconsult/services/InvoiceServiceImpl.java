package com.example.coconsult.services;

import com.example.coconsult.Repository.InvoiceRepository;
import com.example.coconsult.entities.Invoice;
import com.example.coconsult.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice getInvoiceById(String id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        invoice.setStatus(Invoice.InvoiceStatus.NEW);
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice updateInvoice(String id, Invoice invoice) {
        Invoice existingInvoice = getInvoiceById(id);

        // Ne pas permettre de changer le statut via cette méthode
        Invoice.InvoiceStatus originalStatus = existingInvoice.getStatus();

        // Mise à jour des champs (sauf statut)
        existingInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
        existingInvoice.setVendorName(invoice.getVendorName());
        existingInvoice.setAmount(invoice.getAmount());
        existingInvoice.setInvoiceDate(invoice.getInvoiceDate());
        existingInvoice.setDueDate(invoice.getDueDate());
        existingInvoice.setDescription(invoice.getDescription());

        // Restaurer le statut original
        existingInvoice.setStatus(originalStatus);

        return invoiceRepository.save(existingInvoice);
    }

    @Override
    public void deleteInvoice(String id) {
        Invoice invoice = getInvoiceById(id);
        // Vérifier que seules les factures NEW peuvent être supprimées
        if (invoice.getStatus() != Invoice.InvoiceStatus.NEW) {
            throw new RuntimeException("Only invoices with NEW status can be deleted");
        }
        invoiceRepository.deleteById(id);
    }

    @Override
    public List<Invoice> getInvoicesByStatus(Invoice.InvoiceStatus status) {
        return invoiceRepository.findByStatus(status);
    }

    @Override
    public Invoice validateInvoice(String id, User validator) {
        Invoice invoice = getInvoiceById(id);

        if (invoice.getStatus() != Invoice.InvoiceStatus.NEW) {
            throw new RuntimeException("Only NEW invoices can be validated");
        }

        invoice.setStatus(Invoice.InvoiceStatus.VALIDATED);
        invoice.setValidatedBy(validator);
        invoice.setValidationDate(LocalDate.now());

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice rejectInvoice(String id, String reason, User validator) {
        Invoice invoice = getInvoiceById(id);

        if (invoice.getStatus() != Invoice.InvoiceStatus.NEW) {
            throw new RuntimeException("Only NEW invoices can be rejected");
        }

        invoice.setStatus(Invoice.InvoiceStatus.REJECTED);
        invoice.setRejectionReason(reason);
        invoice.setValidatedBy(validator);
        invoice.setValidationDate(LocalDate.now());

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice archiveInvoice(String id) {
        Invoice invoice = getInvoiceById(id);

        if (invoice.getStatus() != Invoice.InvoiceStatus.VALIDATED) {
            throw new RuntimeException("Only VALIDATED invoices can be archived");
        }

        invoice.setStatus(Invoice.InvoiceStatus.ARCHIVED);
        invoice.setArchiveDate(LocalDate.now());

        return invoiceRepository.save(invoice);
    }

    @Override
    public List<Invoice> searchInvoices(String keyword) {
        return invoiceRepository.findByVendorNameContainingIgnoreCase(keyword);
    }
}