package com.example.coconsult.services;

import com.example.coconsult.Repository.DepartmentRepository;
import com.example.coconsult.Repository.UserRepository;
import com.example.coconsult.entities.Department;
import com.example.coconsult.entities.HiringDecision;
import com.example.coconsult.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Service
public class StaffingAnalysisService {
    private final UserRepository userRepository;

    @Autowired
    DepartmentRepository departmentRepository;
    private final double PERFORMANCE_THRESHOLD = 70.0;
    private final double WORKLOAD_THRESHOLD = 1.2; // Seuil de charge de travail
    private final int MIN_STAFF_PER_DEPT = 3;

    @Autowired
    public StaffingAnalysisService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }




    /**
     * Analyse globale des besoins en personnel
     */
    public HiringDecision analyzeHiringNeeds() {
        List<User> activeUsers = userRepository.findByEmploymentStatus("ACTIVE");

        // Map departmentId to departmentName for each user
        Map<String, String> departmentIdToName = activeUsers.stream()
                .filter(user -> user.getDepartmentId() != null)
                .distinct()
                .map(User::getDepartmentId)
                .collect(Collectors.toMap(
                        id -> id,
                        id -> departmentRepository.findById(id)
                                .map(Department::getName)
                                .orElse("Unknown"),
                        (existing, replacement) -> existing // Handle duplicates
                ));

        // Group by department name (instead of departmentId)
        Map<String, Long> staffPerDept = activeUsers.stream()
                .filter(user -> user.getDepartmentId() != null)
                .collect(Collectors.groupingBy(
                        user -> departmentIdToName.getOrDefault(user.getDepartmentId(), "Unknown"),
                        Collectors.counting()
                ));

        Map<String, Double> avgPerformancePerDept = activeUsers.stream()
                .filter(user -> user.getPerformanceScore() != null && user.getDepartmentId() != null)
                .collect(Collectors.groupingBy(
                        user -> departmentIdToName.getOrDefault(user.getDepartmentId(), "Unknown"),
                        Collectors.averagingDouble(User::getPerformanceScore)
                ));

        HiringDecision decision = new HiringDecision();
        decision.setMetrics(new HashMap<>());

        // 1. Check for understaffed departments
        boolean understaffed = staffPerDept.entrySet().stream()
                .anyMatch(entry -> entry.getValue() < MIN_STAFF_PER_DEPT);

        // 2. Check for low average performance
        boolean lowPerformance = avgPerformancePerDept.entrySet().stream()
                .anyMatch(entry -> entry.getValue() < PERFORMANCE_THRESHOLD);

        // 3. Calculate theoretical workload
        Map<String, Double> workloadMetrics = calculateWorkload(activeUsers);
        boolean highWorkload = workloadMetrics.values().stream()
                .anyMatch(ratio -> ratio > WORKLOAD_THRESHOLD);

        decision.setNeedsMoreEmployees(understaffed || lowPerformance || highWorkload);

        if (decision.isNeedsMoreEmployees()) {
            String problematicDepts = identifyProblematicDepartments(
                    staffPerDept, avgPerformancePerDept, workloadMetrics);

            decision.setDepartment(problematicDepts);
            decision.setJustification(buildJustification(
                    understaffed, lowPerformance, highWorkload));
            decision.setRecommendedAction("Recruter dans les départements: " + problematicDepts);
        } else {
            decision.setJustification("Effectifs et performances adéquats dans tous les départements");
            decision.setRecommendedAction("Maintenir le statu quo");
        }

        // Add metrics
        decision.getMetrics().put("min_staff_per_dept", (double) MIN_STAFF_PER_DEPT);
        decision.getMetrics().put("performance_threshold", PERFORMANCE_THRESHOLD);
        decision.getMetrics().put("workload_threshold", WORKLOAD_THRESHOLD);
        avgPerformancePerDept.forEach((dept, score) ->
                decision.getMetrics().put("dept_" + dept + "_avg_perf", score));
        staffPerDept.forEach((dept, count) ->
                decision.getMetrics().put("dept_" + dept + "_staff_count", count.doubleValue()));

        return decision;
    }

    /**
     * Analyse spécifique par département
     */
    public HiringDecision analyzeDepartmentNeeds(String department) {
        List<User> deptUsers = userRepository.findByDepartmentIdAndEmploymentStatus(department, "ACTIVE");

        HiringDecision decision = new HiringDecision();
        decision.setDepartment(department);
        decision.setMetrics(new HashMap<>());

        long staffCount = deptUsers.size();
        double avgPerformance = deptUsers.stream()
                .filter(u -> u.getPerformanceScore() != null)
                .mapToDouble(User::getPerformanceScore)
                .average()
                .orElse(100.0); // Default to 100 if no scores

        double workloadRatio = calculateDepartmentWorkload(deptUsers);

        decision.getMetrics().put("staff_count", (double) staffCount);
        decision.getMetrics().put("avg_performance", avgPerformance);
        decision.getMetrics().put("workload_ratio", workloadRatio);

        boolean needsMore = staffCount < MIN_STAFF_PER_DEPT
                || avgPerformance < PERFORMANCE_THRESHOLD
                || workloadRatio > WORKLOAD_THRESHOLD;

        decision.setNeedsMoreEmployees(needsMore);

        if (needsMore) {
            StringBuilder justification = new StringBuilder();
            if (staffCount < MIN_STAFF_PER_DEPT) {
                justification.append(String.format(
                        "Sous-effectif (%d/%d). ", staffCount, MIN_STAFF_PER_DEPT));
            }
            if (avgPerformance < PERFORMANCE_THRESHOLD) {
                justification.append(String.format(
                        "Performance moyenne faible (%.1f/%.1f). ", avgPerformance, PERFORMANCE_THRESHOLD));
            }
            if (workloadRatio > WORKLOAD_THRESHOLD) {
                justification.append(String.format(
                        "Charge de travail élevée (%.2f/%.2f). ", workloadRatio, WORKLOAD_THRESHOLD));
            }

            decision.setJustification(justification.toString());
            decision.setRecommendedAction("Recruter dans le département " + department);
        } else {
            decision.setJustification("Effectif et performance adéquats");
            decision.setRecommendedAction("Pas besoin de recrutement immédiat");
        }

        return decision;
    }

    private Map<String, Double> calculateWorkload(List<User> users) {
        // Map departmentId to departmentName
        Map<String, String> departmentIdToName = users.stream()
                .filter(user -> user.getDepartmentId() != null)
                .distinct()
                .map(User::getDepartmentId)
                .collect(Collectors.toMap(
                        id -> id,
                        id -> departmentRepository.findById(id)
                                .map(Department::getName)
                                .orElse("Unknown"),
                        (existing, replacement) -> existing // Handle duplicates
                ));

        return users.stream()
                .filter(user -> user.getDepartmentId() != null)
                .collect(Collectors.groupingBy(
                        user -> departmentIdToName.getOrDefault(user.getDepartmentId(), "Unknown"),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                this::calculateDepartmentWorkload)
                ));
    }

    private double calculateDepartmentWorkload(List<User> deptUsers) {
        if (deptUsers.isEmpty()) return 0.0;

        // 1. Performance Factor (40%)
        double performanceFactor = deptUsers.stream()
                .filter(u -> u.getPerformanceScore() != null)
                .mapToDouble(u -> (100 - u.getPerformanceScore()) / 100)
                .average()
                .orElse(0.0);

        // 2. Tenure Factor (30%)
        double tenureFactor = deptUsers.stream()
                .filter(u -> u.getHireDate() != null)
                .mapToDouble(u -> {
                    double years = Period.between(u.getHireDate(), LocalDate.now()).getYears();
                    return Math.min(1.0, years / 5.0);
                })
                .average()
                .orElse(0.0);
        tenureFactor = 1 - tenureFactor;

        // 3. Training Factor (30%)
        long trainingsInProgress = deptUsers.stream()
                .flatMap(u -> u.getTrainings() != null ? u.getTrainings().stream() : Stream.empty())
                .filter(t -> "IN_PROGRESS".equals(t.getStatus()))
                .count();
        double trainingFactor = trainingsInProgress / (double) deptUsers.size();

        return (0.4 * performanceFactor + 0.3 * tenureFactor + 0.3 * trainingFactor) * 2;
    }

    private String identifyProblematicDepartments(
            Map<String, Long> staffCounts,
            Map<String, Double> performances,
            Map<String, Double> workloads) {

        return staffCounts.entrySet().stream()
                .filter(entry ->
                        entry.getValue() < MIN_STAFF_PER_DEPT ||
                                performances.getOrDefault(entry.getKey(), 100.0) < PERFORMANCE_THRESHOLD ||
                                workloads.getOrDefault(entry.getKey(), 0.0) > WORKLOAD_THRESHOLD)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));
    }

    private String buildJustification(boolean understaffed, boolean lowPerformance, boolean highWorkload) {
        StringBuilder sb = new StringBuilder();
        if (understaffed) {
            sb.append("Certains départements ont moins de ").append(MIN_STAFF_PER_DEPT).append(" employés. ");
        }
        if (lowPerformance) {
            sb.append("Performances moyennes inférieures à ").append(PERFORMANCE_THRESHOLD).append("% dans certains départements. ");
        }
        if (highWorkload) {
            sb.append("Charge de travail élevée dans certains départements. ");
        }
        return sb.toString();
    }
}
