package com.example.coconsult.entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "non_conformances")
public class NonConformance {
    @Id
    private String id; // Identifiant unique généré par MongoDB
    private String title; // Titre de la non-conformité
    private String description; // Description détaillée
    private String status; // Statut (ex: "OPEN", "IN_PROGRESS", "CLOSED")
    private LocalDateTime createdAt; // Date de création
    private LocalDateTime updatedAt; // Date de dernière mise à jour
    private String assignedTo; // Personne assignée (optionnel)

    // Constructeurs
    public NonConformance() {}

    public NonConformance(String title, String description, String status, LocalDateTime createdAt, LocalDateTime updatedAt, String assignedTo) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.assignedTo = assignedTo;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}