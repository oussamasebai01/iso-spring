package com.example.coconsult.services;

import com.example.coconsult.Repository.DepartmentRepository;
import com.example.coconsult.Repository.UserRepository;
import com.example.coconsult.entities.Department;
import com.example.coconsult.entities.Salary;
import com.example.coconsult.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SalaryService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    // Configuration des salaires de base par département
    private final Map<String, Double> departmentBaseSalaries = new HashMap<>();
    // Multiplicateurs d'expérience
    private final Map<String, Double> experienceMultipliers = new HashMap<>();

    // Seuil et taux de bonus de performance
    private static final double PERFORMANCE_BONUS_THRESHOLD = 80.0;
    private static final double PERFORMANCE_BONUS_RATE = 0.15;

    @Autowired
    public SalaryService(UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;

        // Initialisation des configurations (pourraient venir d'une base de données)
        initializeSalaryConfigurations();
    }

    private void initializeSalaryConfigurations() {
        // Salaires de base par département
        departmentBaseSalaries.put("IT", 5000.0);
        departmentBaseSalaries.put("HR", 4000.0);
        departmentBaseSalaries.put("Finance", 4500.0);
        departmentBaseSalaries.put("Sales", 3500.0);

        // Multiplicateurs d'expérience
        experienceMultipliers.put("Junior", 1.0);
        experienceMultipliers.put("Intermediate", 1.2);
        experienceMultipliers.put("Senior", 1.5);
        experienceMultipliers.put("Expert", 2.0);
    }

    /**
     * Calcule le salaire pour un utilisateur donné
     */
    public Salary calculateSalary(User user) {
        // 1. Déterminer le salaire de base selon le département
        Double baseSalary = determineBaseSalary(user);

        // 2. Appliquer le multiplicateur d'expérience
        baseSalary = applyExperienceMultiplier(user, baseSalary);

        // 3. Calculer le bonus de performance
        Double bonus = calculatePerformanceBonus(user, baseSalary);

        // 4. Créer et retourner l'objet Salary
        return new Salary(baseSalary, bonus, "MONTHLY");
    }
    private Double determineBaseSalary(User user) {
        String departmentId = user.getDepartmentId();
        if (departmentId == null) {
            return 3000.0; // Default salary if departmentId is null
        }

        Department department = departmentRepository.findById(departmentId)
                .orElse(null);
        if (department == null) {
            return 3000.0; // Default salary if department not found
        }

        String departmentName = department.getName();
        return departmentBaseSalaries.getOrDefault(departmentName, 3000.0);
    }

    private Double applyExperienceMultiplier(User user, Double baseSalary) {
        String experienceLevel = user.getExperience();
        if (experienceLevel == null) {
            experienceLevel = "Junior"; // Valeur par défaut
        }

        Double multiplier = experienceMultipliers.getOrDefault(experienceLevel, 1.0);
        return baseSalary * multiplier;
    }

    private Double calculatePerformanceBonus(User user, Double baseSalary) {
        if (user.getPerformanceScore() == null || user.getPerformanceScore() < PERFORMANCE_BONUS_THRESHOLD) {
            return 0.0;
        }

        return baseSalary * PERFORMANCE_BONUS_RATE;
    }

    /**
     * Calcule et met à jour le salaire pour un utilisateur
     */
    public User calculateAndUpdateSalary(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getDepartmentId() == null) {
            throw new RuntimeException("User departmentId is not set");
        }

        Department department = departmentRepository.findById(user.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found for id: " + user.getDepartmentId()));

        Salary salary = calculateSalary(user);
        user.setSalary(salary);

        return userRepository.save(user);
    }
    /**
     * Calcule et met à jour les salaires pour tous les utilisateurs actifs
     */
    public void calculateSalariesForAllActiveUsers() {
        userRepository.findByEmploymentStatus("ACTIVE").forEach(user -> {
            Salary salary = calculateSalary(user);
            user.setSalary(salary);
            userRepository.save(user);
        });
    }


}
