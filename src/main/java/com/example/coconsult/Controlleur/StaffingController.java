package com.example.coconsult.Controlleur;

import com.example.coconsult.entities.HiringDecision;
import com.example.coconsult.services.StaffingAnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staffing")
public class StaffingController {

    private final StaffingAnalysisService staffingService;

    public StaffingController(StaffingAnalysisService staffingService) {
        this.staffingService = staffingService;
    }

    @GetMapping("/analysis")
    public HiringDecision analyzeHiringNeeds() {
        return staffingService.analyzeHiringNeeds();
    }

    @GetMapping("/analysis/{department}")
    public HiringDecision analyzeDepartmentNeeds(@PathVariable String department) {
        return staffingService.analyzeDepartmentNeeds(department);
    }
}
