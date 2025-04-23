package com.example.coconsult.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "users")
public class User {

    @Id
    private String userId;

    @Indexed(unique = true)
    private String identifiant;

    private String nom;
    private String prenom;

    @Indexed(unique = true)
    private String email;

    private String experience;
    private String password;
    private String departmentId; // Reference to Department's ID
    private String employmentStatus; // ACTIVE, TERMINATED
    private LocalDate hireDate;
    private LocalDate terminationDate;
    private Salary salary; // Salary information
    private List<TrainingRecord> trainings = new ArrayList<>(); // Embedded training data
    private Double performanceScore; // Simple performance metric (0-100)
    private List<String> roles = new ArrayList<>();

    // Embedded class for training records
    public static class TrainingRecord {
        private String title;
        private String status; // PLANNED, IN_PROGRESS, COMPLETED
        private LocalDate completionDate;

        public TrainingRecord() {}

        public TrainingRecord(String title, String status, LocalDate completionDate) {
            this.title = title;
            this.status = status;
            this.completionDate = completionDate;
        }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public LocalDate getCompletionDate() { return completionDate; }
        public void setCompletionDate(LocalDate completionDate) { this.completionDate = completionDate; }
    }

    // Constructors
    public User() {}

    public User(String identifiant, String nom, String prenom, String email,
                String experience, String password, String departmentId,
                String employmentStatus, LocalDate hireDate) {
        this.identifiant = identifiant;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.experience = experience;
        this.password = password;
        this.departmentId = departmentId;
        this.employmentStatus = employmentStatus;
        this.hireDate = hireDate;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getIdentifiant() { return identifiant; }
    public void setIdentifiant(String identifiant) { this.identifiant = identifiant; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public String getEmploymentStatus() { return employmentStatus; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public LocalDate getTerminationDate() { return terminationDate; }
    public void setTerminationDate(LocalDate terminationDate) { this.terminationDate = terminationDate; }

    public Salary getSalary() { return salary; }
    public void setSalary(Salary salary) { this.salary = salary; }

    public List<TrainingRecord> getTrainings() { return trainings; }
    public void setTrainings(List<TrainingRecord> trainings) { this.trainings = trainings; }

    public Double getPerformanceScore() { return performanceScore; }
    public void setPerformanceScore(Double performanceScore) { this.performanceScore = performanceScore; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    // Helper methods
    public void addTraining(TrainingRecord training) {
        if (this.trainings == null) {
            this.trainings = new ArrayList<>();
        }
        this.trainings.add(training);
    }

    public void addRole(String role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", identifiant='" + identifiant + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", experience='" + experience + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", employmentStatus='" + employmentStatus + '\'' +
                ", performanceScore=" + performanceScore +
                '}';
    }
}