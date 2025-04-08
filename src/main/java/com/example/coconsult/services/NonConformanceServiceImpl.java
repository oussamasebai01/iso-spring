package com.example.coconsult.services;


import com.example.coconsult.Repository.NonConformanceRepository;
import com.example.coconsult.entities.NonConformance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NonConformanceServiceImpl implements NonConformanceService {

    @Autowired
    private NonConformanceRepository nonConformanceRepository;

    @Override
    public NonConformance createNonConformance(NonConformance nonConformance) {
        nonConformance.setCreatedAt(LocalDateTime.now());
        nonConformance.setUpdatedAt(LocalDateTime.now());
        return nonConformanceRepository.save(nonConformance);
    }

    @Override
    public NonConformance updateNonConformance(String id, NonConformance nonConformanceDetails) {
        Optional<NonConformance> optionalNonConformance = nonConformanceRepository.findById(id);
        if (optionalNonConformance.isPresent()) {
            NonConformance existingNonConformance = optionalNonConformance.get();
            existingNonConformance.setTitle(nonConformanceDetails.getTitle());
            existingNonConformance.setDescription(nonConformanceDetails.getDescription());
            existingNonConformance.setStatus(nonConformanceDetails.getStatus());
            existingNonConformance.setAssignedTo(nonConformanceDetails.getAssignedTo());
            existingNonConformance.setUpdatedAt(LocalDateTime.now());
            return nonConformanceRepository.save(existingNonConformance);
        } else {
            throw new RuntimeException("Non-conformité non trouvée avec l'ID : " + id);
        }
    }

    @Override
    public List<NonConformance> getAllNonConformances() {
        return nonConformanceRepository.findAll();
    }

    @Override
    public List<NonConformance> getNonConformancesByStatus(String status) {
        return nonConformanceRepository.findByStatus(status);
    }

    @Override
    public NonConformance getNonConformanceById(String id) {
        return nonConformanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Non-conformité non trouvée avec l'ID : " + id));
    }

    @Override
    public void deleteNonConformanceById(String id) {
        nonConformanceRepository.deleteById(id);
    }
}