package com.example.dashboard_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dashboard_service.repository.NicRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.dashboard_service.model.NicRecord;

@Service
public class DashService {
    @Autowired
    private NicRepository nicRecordRepository;

    public long getTotalRecords() {
        return nicRecordRepository.countTotalRecords();
    }

    public long getMaleUsers() {
        return nicRecordRepository.countMaleUsers();
    }

    public long getFemaleUsers() {
        return nicRecordRepository.countFemaleUsers();

    }
    
    public List<NicRecord> getAllRecords() {
        return nicRecordRepository.findAll();
    }

    public Map<String, Long> getRecordsByFileName() {
        List<Object[]> results = nicRecordRepository.countRecordsByFileName();
        Map<String, Long> recordsByFileName = new HashMap<>();
        for (Object[] result : results) {
            recordsByFileName.put((String) result[0], (Long) result[1]);
        }
        return recordsByFileName;
    }
}
