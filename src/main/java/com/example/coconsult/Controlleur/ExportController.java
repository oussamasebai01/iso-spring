package com.example.coconsult.Controlleur;

import com.example.coconsult.services.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders; // Import correct
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping("/invoices/csv")
    public ResponseEntity<String> exportInvoices() {
        String csv = exportService.exportInvoicesToCsv();
        HttpHeaders headers = new HttpHeaders(); // Instance de org.springframework.http.HttpHeaders
        headers.setContentType(MediaType.TEXT_PLAIN); // Définit le type de contenu
        headers.setContentDispositionFormData("attachment", "invoices.csv"); // Définit le nom du fichier
        return new ResponseEntity<>(csv, headers, HttpStatus.OK);
    }
}