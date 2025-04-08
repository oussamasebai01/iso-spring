package com.example.coconsult.Repository;


import com.example.coconsult.entities.NonConformance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NonConformanceRepository extends MongoRepository<NonConformance, String> {
    // Méthode personnalisée pour rechercher par statut
    List<NonConformance> findByStatus(String status);
}