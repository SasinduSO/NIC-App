package com.example.dashboard_service.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dashboard_service.service.DashService;
import com.example.dashboard_service.service.ReportService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:3000")  // Ensure CORS is allowed for this origin
public class DashController {

    @Autowired
    private DashService dashboardService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalRecords", dashboardService.getTotalRecords());
        summary.put("maleUsers", dashboardService.getMaleUsers());
        summary.put("femaleUsers", dashboardService.getFemaleUsers());
        summary.put("recordsByFileName", dashboardService.getRecordsByFileName());
        summary.put("totalInvalidRecords", dashboardService.getTotalInvalidRecords());
        summary.put("invalidRecordsByFileName", dashboardService.getInvalidsByFileName());
        summary.put("ageDistributionPercentages", dashboardService.getAgeDistributionPercentages());

        return summary;
    }
    @GetMapping("/generate-report")
    public ResponseEntity<byte[]> generateReport(
        @RequestParam String format,
        @RequestParam(required = false) boolean includeFemaleNics,
        @RequestParam(required = false) boolean includeMaleNics,
        @RequestParam(required = false) boolean includeTotalRecords,
        @RequestParam(required = false) boolean includeInvalidRecords,
        @RequestParam(required = false) boolean includeTotalInvalidRecords,
        @RequestParam(required = false) boolean includeTotalValidRecords
    ) throws IOException {
    
        ByteArrayInputStream report = reportService.generateReport(
            format, 
            includeFemaleNics, 
            includeMaleNics, 
            includeTotalRecords, 
            includeInvalidRecords,
            includeTotalInvalidRecords,
            includeTotalValidRecords
        );
    
        HttpHeaders headers = new HttpHeaders();
        String contentType;
        String extension;
    
        switch (format.toLowerCase()) {
            case "pdf" -> {
                contentType = "application/pdf";
                extension = "pdf";
            }
            case "csv" -> {
                contentType = "text/csv";
                extension = "csv";
            }
            case "xlsx" -> {
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                extension = "xlsx";
            }
            default -> throw new IllegalArgumentException("Invalid format: " + format);
        }
    
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.add("Content-Disposition", "attachment; filename=nic_report." + extension);
    
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(report.readAllBytes());
    }
    
}
