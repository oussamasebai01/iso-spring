package com.example.coconsult.entities;

import lombok.Data;
import java.time.LocalDate;
import java.util.Map;

@Data
public class HiringDecision {
    private boolean needsMoreEmployees;
    private String department;
    private String justification;
    private Map<String, Double> metrics; // Métriques utilisées pour la décision
    private LocalDate analysisDate;
    private String recommendedAction;

    public HiringDecision() {
        this.analysisDate = LocalDate.now();
    }
}