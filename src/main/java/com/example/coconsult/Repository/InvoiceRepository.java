package com.example.coconsult.Repository;

import com.example.coconsult.entities.Invoice;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {

    List<Invoice> findByStatus(Invoice.InvoiceStatus status);

    List<Invoice> findByVendorNameContainingIgnoreCase(String vendorName);

    List<Invoice> findByValidatedBy_UserId(String userId);
}