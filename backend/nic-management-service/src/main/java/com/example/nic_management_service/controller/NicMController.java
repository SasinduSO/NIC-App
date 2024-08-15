package com.example.nic_management_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.nic_management_service.model.InvalidNic;
import com.example.nic_management_service.model.NicM;
import com.example.nic_management_service.service.NicMService;

@RestController
@RequestMapping("api/nicM")
public class NicMController {

    @Autowired
    private NicMService nicMService;

    @GetMapping("/all")
    public List<NicM> getAllNics() {

        return nicMService.getAllNics();

    }

    @PostMapping("/save")
    public ResponseEntity<Void> saveNic(@RequestBody List<NicM> nics) {
        for (NicM nic : nics) {
            NicM nicM = new NicM();
            nicM.setNic_no(nic.getNic_no());
            nicM.setGender(nic.getGender());
            nicM.setBirthDate(nic.getBirthDate());
            nicM.setAge(nic.getAge());
            nicM.setFileName(nic.getFileName());
            nicMService.saveNicM(nicM);

        }
        return ResponseEntity.ok().build();
    }

    
    @PostMapping("/InvalidSave")
    public ResponseEntity<Void> InvalidSave(@RequestBody List<InvalidNic> iNics){
        for(InvalidNic inic : iNics){
            InvalidNic iNicM = new InvalidNic();
            iNicM.setNic_no(inic.getNic_no());
            iNicM.setFileName(inic.getFileName());
            nicMService.InvalidSave(iNicM);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/file")
    public ResponseEntity<List<NicM>> getNicsByFileName(@RequestBody String fileName) {
        List<NicM> stored = nicMService.getByFilename(fileName.trim());
        return ResponseEntity.ok(stored);
    }

    @GetMapping("/api/status")
    public String getStatus() {
        return "API is working!";
    }
}
