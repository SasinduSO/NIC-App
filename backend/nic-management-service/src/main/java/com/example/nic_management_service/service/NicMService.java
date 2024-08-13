package com.example.nic_management_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.nic_management_service.model.NicM;
import com.example.nic_management_service.repository.NicMRepository;



@Service
public class NicMService {

    //api passers
    //sql passers
    @Autowired
    private NicMRepository nicMRepository;
    //constructor
    //intialized list
   // List<NicM> stores = new ArrayList<>();

    //service class
    public List<NicM> getByFilename(String fileName) {
        return nicMRepository.findByFileName(fileName); // Assumes this returns List<NicM>
    }
    //supporter classes
    // Get all NIC records from the database
    public List<NicM> getAllNics() {
        return nicMRepository.findAll();
    }

}
