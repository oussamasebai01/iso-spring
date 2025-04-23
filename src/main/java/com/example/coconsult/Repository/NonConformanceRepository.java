package com.example.coconsult.Repository;


import com.example.coconsult.entities.NonConformance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NonConformanceRepository extends MongoRepository<NonConformance, String> {
    // Méthode personnalisée pour rechercher par statut
    List<NonConformance> findByStatus(String status);
}