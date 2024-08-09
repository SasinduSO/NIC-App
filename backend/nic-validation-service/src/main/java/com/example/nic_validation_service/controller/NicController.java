package com.example.nic_validation_service.controller;

import com.example.nic_validation_service.model.Nic;
import com.example.nic_validation_service.service.NicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.nic_validation_service.exception.InvalidFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
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
        List<Nic> allNics = new ArrayList<>();

        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();  // Get the name of the file
            List<String> nicNumbers = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    nicNumbers.add(line.trim());
                }
            } catch (IOException e) {
                throw new InvalidFileException("An error occurred while processing the file: " + filename+ "ERROR:"+ e);

            }

            // Pass the NIC numbers along with the filename to the service
            List<Nic> nicsFromThisFile = nicService.parseNicsFromCsv(nicNumbers, filename);
            allNics.addAll(nicsFromThisFile); //ads all parsed nics to this new arraylsit
        }

        return allNics;
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "API is working!";
    }
}
