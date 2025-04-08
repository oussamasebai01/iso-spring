package com.example.coconsult.Controlleur;



import com.example.coconsult.entities.Invoice;
import com.example.coconsult.entities.User;
import com.example.coconsult.services.InvoiceService;

import com.example.coconsult.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('FINANCIAL_MANAGER')")
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('FINANCIAL_MANAGER')")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable String id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('FINANCIAL_MANAGER')")
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        return new ResponseEntity<>(invoiceService.createInvoice(invoice), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FINANCIAL_MANAGER')")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable String id, @RequestBody Invoice invoice) {
        return ResponseEntity.ok(invoiceService.updateInvoice(id, invoice));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FINANCIAL_MANAGER')")
    public ResponseEntity<Void> deleteInvoice(@PathVariable String id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('FINANCIAL_MANAGER')")
    public ResponseEntity<List<Invoice>> getInvoicesByStatus(@PathVariable String status) {
        try {
            Invoice.InvoiceStatus invoiceStatus = Invoice.InvoiceStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(invoiceService.getInvoicesByStatus(invoiceStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/validate")
    @PreAuthorize("hasRole('FINANCIAL_MANAGER')")
    public ResponseEntity<Invoice> validateInvoice(@PathVariable String id, Authentication authentication) {
        String username = authentication.getName();
        User validator = userService.getUserByIdentifiant(username);
        return ResponseEntity.ok(invoiceService.validateInvoice(id, validator));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('FINANCIAL_MANAGER')")
    public ResponseEntity<Invoice> rejectInvoice(@PathVariable String id, @RequestBody Map<String, String> payload,
                                                 Authentication authentication) {
        String reason = payload.get("reason");
        if (reason == null || reason.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String username = authentication.getName();
        User validator = userService.getUserByIdentifiant(username);
        return ResponseEntity.ok(invoiceService.rejectInvoice(id, reason, validator));
    }

    @PostMapping("/{id}/archive")
    @PreAuthorize("hasRole('FINANCIAL_MANAGER')")
    public ResponseEntity<Invoice> archiveInvoice(@PathVariable String id) {
        return ResponseEntity.ok(invoiceService.archiveInvoice(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('FINANCIAL_MANAGER')")
    public ResponseEntity<List<Invoice>> searchInvoices(@RequestParam String keyword) {
        return ResponseEntity.ok(invoiceService.searchInvoices(keyword));
    }
}