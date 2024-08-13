package com.example.nic_management_service.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.nic_management_service.model.NicM;
import com.example.nic_management_service.service.NicMService;


@RestController
@RequestMapping("api/nicM")
public class NicMController {


    @Autowired
    private NicMService nicMService;

    @GetMapping("/all")
    public List<NicM> getAllNics(){

        return nicMService.getAllNics();

    }

    @PostMapping("/file")
    public ResponseEntity<List<NicM>> getNicsByFileName(@RequestBody String fileName) {
        List<NicM> stored = nicMService.getByFilename(fileName.trim());
        return ResponseEntity.ok(stored);
    }


    @GetMapping("/api/status")
    public String getStatus() {
        return "API is working!";
    }
}
