package com.example.coconsult.services;

import com.example.coconsult.entities.Invoice;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;

@Service
public class ExportService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public String exportInvoicesToCsv() {
        List<Invoice> invoices = mongoTemplate.findAll(Invoice.class);
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);

        // En-tête
        String[] header = {"Numéro", "Fournisseur", "Montant", "Date", "Échéance", "Statut"};
        csvWriter.writeNext(header);

        // Données
        for (Invoice invoice : invoices) {
            String[] row = {
                    invoice.getInvoiceNumber(),
                    invoice.getVendorName(),
                    String.valueOf(invoice.getAmount()),
                    invoice.getInvoiceDate().toString(),
                    invoice.getDueDate().toString(),
                    String.valueOf(invoice.getStatus())
            };
            csvWriter.writeNext(row);
        }

        return writer.toString();
    }
}