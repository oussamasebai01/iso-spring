package com.example.coconsult.Controlleur;

import com.example.coconsult.services.HRDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/hr/dashboard")
public class HRDashboardController {

    @Autowired
    private HRDashboardService hrDashboardService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboardData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> dashboardData = hrDashboardService.getDashboardData(startDate, endDate);
        return ResponseEntity.ok(dashboardData);
    }
}
