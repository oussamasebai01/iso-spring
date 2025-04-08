package com.example.coconsult.Controlleur;


import com.example.coconsult.entities.NonConformance;
import com.example.coconsult.services.NonConformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/non-conformances")
public class NonConformanceController {

    @Autowired
    private NonConformanceService nonConformanceService; // Utilisation de l'interface

    // Créer une non-conformité
    @PostMapping
    public ResponseEntity<NonConformance> createNonConformance(@RequestBody NonConformance nonConformance) {
        NonConformance createdNonConformance = nonConformanceService.createNonConformance(nonConformance);
        return new ResponseEntity<>(createdNonConformance, HttpStatus.CREATED);
    }

    // Mettre à jour une non-conformité
    @PutMapping("/{id}")
    public ResponseEntity<NonConformance> updateNonConformance(@PathVariable String id, @RequestBody NonConformance nonConformanceDetails) {
        NonConformance updatedNonConformance = nonConformanceService.updateNonConformance(id, nonConformanceDetails);
        return new ResponseEntity<>(updatedNonConformance, HttpStatus.OK);
    }

    // Lister toutes les non-conformités
    @GetMapping
    public ResponseEntity<List<NonConformance>> getAllNonConformances() {
        List<NonConformance> nonConformances = nonConformanceService.getAllNonConformances();
        return new ResponseEntity<>(nonConformances, HttpStatus.OK);
    }

    // Lister les non-conformités par statut
    @GetMapping("/status/{status}")
    public ResponseEntity<List<NonConformance>> getNonConformancesByStatus(@PathVariable String status) {
        List<NonConformance> nonConformances = nonConformanceService.getNonConformancesByStatus(status);
        return new ResponseEntity<>(nonConformances, HttpStatus.OK);
    }

    // Voir une non-conformité spécifique par ID
    @GetMapping("/{id}")
    public ResponseEntity<NonConformance> getNonConformanceById(@PathVariable String id) {
        NonConformance nonConformance = nonConformanceService.getNonConformanceById(id);
        return new ResponseEntity<>(nonConformance, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNonConformance(@PathVariable String id) {
        nonConformanceService.deleteNonConformanceById(id);
        return ResponseEntity.noContent().build();
    }
}