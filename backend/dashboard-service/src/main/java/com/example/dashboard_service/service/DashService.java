package com.example.dashboard_service.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dashboard_service.model.NicInvalid;
import com.example.dashboard_service.model.NicRecord;
import com.example.dashboard_service.repository.NicInvalidRep;
import com.example.dashboard_service.repository.NicRepository;

@Service
public class DashService {
    @Autowired
    private NicRepository nicRecordRepository;

    @Autowired
    private NicInvalidRep nicInvalidRep;

    public long getTotalRecords() {
        return nicRecordRepository.countTotalRecords();
    }

    public long getTotalInvalidRecords() {
        return nicInvalidRep.countTotalInvalid();
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

    public List<NicInvalid> getAllInvalidRecords() {
        return nicInvalidRep.findAll();
    }

    public Map<String, Long> getRecordsByFileName() {
        List<Object[]> results = nicRecordRepository.countRecordsByFileName();
        Map<String, Long> recordsByFileName = new HashMap<>();
        for (Object[] result : results) {
            recordsByFileName.put((String) result[0], (Long) result[1]);
        }
        return recordsByFileName;
    }

    public Map<String, Long> getInvalidsByFileName() {
        List<Object[]> results = nicInvalidRep.countInvalidsByFileName();
        Map<String, Long> invalidRecordsByFileName = new HashMap<>();
        for (Object[] result : results) {
            invalidRecordsByFileName.put((String) result[0], (Long) result[1]);
        }
        return invalidRecordsByFileName;
    }

     // Get percentage of people in specific age ranges
     public Map<String, Long> getAgeDistributionPercentages() {
        List<Object[]> results = nicRecordRepository.countByAgeRanges();
        Map<String, Long> ageDistributionMap = new HashMap<>();
    
        for (Object[] result : results) {
            String ageRange = (String) result[0];
            Long count = (Long) result[1];
            ageDistributionMap.put(ageRange, count);
        }
    
        return ageDistributionMap;
    }
    

}
