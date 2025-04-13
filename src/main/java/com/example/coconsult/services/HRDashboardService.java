package com.example.coconsult.services;

import com.example.coconsult.Repository.UserRepository;
import com.example.coconsult.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HRDashboardService {

    @Autowired
    private UserRepository userRepository;


    // Generate dashboard data
    public Map<String, Object> getDashboardData(LocalDate startDate, LocalDate endDate) {
        // Validate dates
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Invalid date range: startDate must be before endDate");
        }

        Map<String, Object> dashboard = new HashMap<>();

        // Get all departments
        List<String> departments = userRepository.findAll().stream()
                .map(User::getDepartment)
                .filter(dept -> dept != null && !dept.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        // Calculate metrics per department
        Map<String, Map<String, Object>> departmentMetrics = new HashMap<>();
        for (String department : departments) {
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("turnoverRate", calculateTurnoverRate(department, startDate, endDate));
            metrics.put("trainingCompletionRate", calculateTrainingCompletionRate(department,startDate,endDate));
            metrics.put("performanceMetrics", calculatePerformanceMetrics(department));
            departmentMetrics.put(department, metrics);
        }

        dashboard.put("departments", departmentMetrics);
        dashboard.put("overallTurnoverRate", calculateOverallTurnoverRate(startDate, endDate));
        dashboard.put("overallTrainingCompletionRate", calculateOverallTrainingCompletionRate());
        dashboard.put("overallAveragePerformance", calculateOverallAveragePerformance());

        return dashboard;
    }
    // Calculate turnover rate for a department
    private Double calculateTurnoverRate(String department, LocalDate startDate, LocalDate endDate) {
        List<User> departmentUsers = userRepository.findByDepartment(department);
        if (departmentUsers.isEmpty()) {
            return 0.0;
        }

        long totalEmployees = departmentUsers.size();
        long terminatedEmployees = departmentUsers.stream()
                .filter(user -> "COMPLETED".equalsIgnoreCase(user.getEmploymentStatus())
                        && user.getTerminationDate() != null
                        && !user.getTerminationDate().isBefore(startDate)
                        && !user.getTerminationDate().isAfter(endDate))
                .count();

        if(totalEmployees > 0)
            return (terminatedEmployees * 100.0) / totalEmployees;
        else return 0.0;

    }

    // Calculate overall turnover rate
    private Double calculateOverallTurnoverRate(LocalDate startDate, LocalDate endDate) {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            return 0.0;
        }

        long totalEmployees = allUsers.size();
        long terminatedEmployees = allUsers.stream()
                .filter(user -> "COMPLETED".equalsIgnoreCase(user.getEmploymentStatus())
                        && user.getTerminationDate() != null
                        && !user.getTerminationDate().isBefore(startDate)
                        && !user.getTerminationDate().isAfter(endDate))
                .count();

        return totalEmployees > 0 ? (terminatedEmployees * 100.0) / totalEmployees : 0.0;
    }

    // Calculate training completion rate for a department
    private Double calculateTrainingCompletionRate(String department, LocalDate startDate, LocalDate endDate) {
        List<User> departmentUsers = userRepository.findByDepartment(department);
        if (departmentUsers.isEmpty()) return 0.0;

        long totalTrainings = departmentUsers.stream()
                .filter(user -> user.getTrainings() != null)
                .flatMap(user -> user.getTrainings().stream())
                .filter(training -> training.getCompletionDate() != null
                        && !training.getCompletionDate().isBefore(startDate)
                        && !training.getCompletionDate().isAfter(endDate))
                .count();

        long completedTrainings = departmentUsers.stream()
                .filter(user -> user.getTrainings() != null)
                .flatMap(user -> user.getTrainings().stream())
                .filter(training -> "COMPLETED".equalsIgnoreCase(training.getStatus())
                        && training.getCompletionDate() != null
                        && !training.getCompletionDate().isBefore(startDate)
                        && !training.getCompletionDate().isAfter(endDate))
                .count();

        return totalTrainings > 0 ? (completedTrainings * 100.0) / totalTrainings : 0.0;
    }

    // Calculate overall training completion rate
    private Double calculateOverallTrainingCompletionRate() {
        List<User> allUsers = userRepository.findAll();
        long totalTrainings = allUsers.stream()
                .filter(user -> user.getTrainings() != null)
                .mapToLong(user -> user.getTrainings().size())
                .sum();

        if (totalTrainings == 0) {
            return 0.0;
        }

        long completedTrainings = allUsers.stream()
                .filter(user -> user.getTrainings() != null)
                .flatMap(user -> user.getTrainings().stream())
                .filter(training -> "COMPLETED".equalsIgnoreCase(training.getStatus()))
                .count();

        return totalTrainings > 0 ? (completedTrainings * 100.0) / totalTrainings : 0.0;
    }

    // Calculate performance metrics for a department
    private Map<String, Double> calculatePerformanceMetrics(String department) {
        List<User> departmentUsers = userRepository.findByDepartment(department);
        Map<String, Double> performanceMetrics = new HashMap<>();

        for (User user : departmentUsers) {
            String fullName = (user.getNom() != null ? user.getNom() : "") + " " +
                    (user.getPrenom() != null ? user.getPrenom() : "");
            Double score = user.getPerformanceScore() != null ? user.getPerformanceScore() : 0.0;
            performanceMetrics.put(fullName.trim(), score);
        }

        return performanceMetrics;
    }

    // Calculate overall average performance
    private Double calculateOverallAveragePerformance() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            return 0.0;
        }

        return allUsers.stream()
                .filter(user -> user.getPerformanceScore() != null)
                .mapToDouble(User::getPerformanceScore)
                .average()
                .orElse(0.0);
    }
}
