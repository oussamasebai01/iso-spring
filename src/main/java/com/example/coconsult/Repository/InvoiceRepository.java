package com.example.coconsult.Repository;

import com.example.coconsult.entities.Invoice;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {

    List<Invoice> findByStatus(Invoice.InvoiceStatus status);

    List<Invoice> findByVendorNameContainingIgnoreCase(String vendorName);

    // Filtrer par période (entre deux dates)
    List<Invoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);

    // Filtrer par département
    List<Invoice> findByDepartment(String department);

    // Filtrer par projet
    List<Invoice> findByProject(String project);


}