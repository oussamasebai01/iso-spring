package com.example.coconsult.Controlleur;

import com.example.coconsult.Repository.UserRepository;
import com.example.coconsult.entities.User;
import com.example.coconsult.services.SalaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/salaries")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;


    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SalaryController.class);

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @GetMapping("/calculate/{userId}")
    public ResponseEntity<?> calculateUserSalary(@PathVariable String userId) {
        try {
            User user = salaryService.calculateAndUpdateSalary(userId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException ex) {
            logger.error("Error calculating salary for user {}", userId, ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message", ex.getMessage(),
                            "timestamp", LocalDateTime.now()
                    ));
        }
    }

    // Recalculer tous les salaires
    @PostMapping("/recalculate-all")
    public ResponseEntity<String> recalculateAllSalaries() {
        salaryService.calculateSalariesForAllActiveUsers();
        return ResponseEntity.ok("Salary calculation completed for all active users");
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
