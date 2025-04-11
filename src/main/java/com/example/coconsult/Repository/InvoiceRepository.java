package com.example.coconsult.Repository;

import com.example.coconsult.entities.Invoice;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {

    List<Invoice> findByStatus(Invoice.InvoiceStatus status);

    List<Invoice> findByVendorNameContainingIgnoreCase(String vendorName);

    List<Invoice> findByValidatedBy_UserId(String userId);

    List<Invoice> findByInvoiceNumberContainingOrVendorNameContaining(String invoiceNumber, String vendorName);

    List<Invoice> findByBudgetBetween(BigDecimal minBudget, BigDecimal maxBudget);

    List<Invoice> findByDepenseBetween(BigDecimal minDepense, BigDecimal maxDepense);

    @Query("{'invoiceDate': {$gte: ?0, $lte: ?1}}")
    List<Invoice> findByInvoiceDateYear(int startYear, int endYear);
}