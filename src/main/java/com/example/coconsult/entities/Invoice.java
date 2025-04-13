package com.example.coconsult.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "invoices")
public class Invoice {

    @Id
    private String id;

    private String invoiceNumber;
    private String vendorName;
    private BigDecimal amount;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private String description;
    private InvoiceStatus status; // NEW, VALIDATED, ARCHIVED, REJECTED
    private String rejectionReason;

    @DBRef
    private User validatedBy;

    private LocalDate validationDate;
    private LocalDate archiveDate;


    private BigDecimal budget;
    private BigDecimal depense;
    private BigDecimal gainAnnuel;

    private String department;
    private String project;

    public enum InvoiceStatus {
        NEW, VALIDATED, ARCHIVED, REJECTED ,PENDING
    }

    public Invoice(String invoiceNumber, String vendorName, BigDecimal amount,
                   LocalDate invoiceDate, LocalDate dueDate, String description) {
        this.invoiceNumber = invoiceNumber;
        this.vendorName = vendorName;
        this.amount = amount;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.description = description;
        this.status = InvoiceStatus.NEW;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public User getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(User validatedBy) {
        this.validatedBy = validatedBy;
    }

    public LocalDate getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(LocalDate validationDate) {
        this.validationDate = validationDate;
    }

    public LocalDate getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(LocalDate archiveDate) {
        this.archiveDate = archiveDate;
    }


}