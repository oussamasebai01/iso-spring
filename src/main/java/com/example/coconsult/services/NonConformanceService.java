package com.example.coconsult.services;

import com.example.coconsult.entities.NonConformance;

import java.util.List;

public interface NonConformanceService {
    // Créer une nouvelle non-conformité
    NonConformance createNonConformance(NonConformance nonConformance);

    // Mettre à jour une non-conformité existante
    NonConformance updateNonConformance(String id, NonConformance nonConformanceDetails);

    // Récupérer toutes les non-conformités
    List<NonConformance> getAllNonConformances();

    // Récupérer les non-conformités par statut
    List<NonConformance> getNonConformancesByStatus(String status);

    // Récupérer une non-conformité par ID
    NonConformance getNonConformanceById(String id);

    void deleteNonConformanceById(String id);
}