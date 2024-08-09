package com.example.nic_validation_service.controller;

import com.example.nic_validation_service.model.Nic;
import com.example.nic_validation_service.service.NicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/nics")
public class NicController {
    @Autowired
    private NicService nicService;

    @PostMapping("/upload")
    public List<Nic> uploadCsvFiles(@RequestParam("files") MultipartFile[] files) {
        List<String> nicNumbers = new ArrayList<>();
        for (MultipartFile file : files) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    nicNumbers.add(line.trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return nicService.parseNicsFromCsv(nicNumbers);
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "API is working!";
    }
}
