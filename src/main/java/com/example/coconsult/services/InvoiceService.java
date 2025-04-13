package com.example.coconsult.services;



import com.example.coconsult.entities.Invoice;
import com.example.coconsult.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InvoiceService {

    List<Invoice> getAllInvoices();

    Invoice getInvoiceById(String id);

    Invoice createInvoice(Invoice invoice);

    Invoice updateInvoice(String id, Invoice invoice);

    void deleteInvoice(String id);

    List<Invoice> getInvoicesByStatus(Invoice.InvoiceStatus status);

    Invoice validateInvoice(String id, User validator);

    Invoice rejectInvoice(String id, String reason, User validator);

    Invoice archiveInvoice(String id);

    List<Invoice> searchInvoices(String keyword);

    public List<Invoice> findByDepartment(String department);
    public List<Invoice> findByProject(String project);

    public List<Invoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);


}