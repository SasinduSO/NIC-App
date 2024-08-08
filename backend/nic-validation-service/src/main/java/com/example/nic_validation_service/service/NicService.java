package com.example.nic_validation_service.service;

import com.example.nic_validation_service.exception.InvalidNicException;
import com.example.nic_validation_service.model.Nic;
import com.example.nic_validation_service.repository.NicRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service // for creating objects
public class NicService {

    //SQL passer
    private NicRepository nicRepository;

    public NicService(NicRepository nicRepository) {
        this.nicRepository = nicRepository;
    }

    // To get and return list of nic numbers from csv
    public List<Nic> parseNicsFromCsv(List<String> nicNumbers) {

        List<Nic> nics = new ArrayList<>(); // creates a new list to hold nic numbers

        // reading csv(now an array) until end of array
        for (String nicNumber : nicNumbers) {

            Nic nic = new Nic(); // new object for each iteration
            nic.setNic_no(nicNumber);
            if (nicNumber.length() == 10) {
                parseOldFormatNic(nic); // passing to old to parse
            } else if (nicNumber.length() == 12) {
                parseNewFormatNic(nic); // [passing ] to new to parse
            } else {
                throw new InvalidNicException("This is an Invalid NIC:" + nicNumber);
            }

            nics.add(nic); // adding to list of objects
            saveNic( nic);
        }
        return nics; // return the lust
    }

    // abstraction methids
    private void parseOldFormatNic(Nic nic) {

        String nicNumber = nic.getNic_no(); // get nic from object

        // parsing nic data
        try {
            int year;
            int yearVal = Integer.parseInt(nicNumber.substring(0, 2));
            if (yearVal >= 10) {
                year = 1900 + yearVal; // if person born before 2000
            } else {
                year = 2000 + yearVal; // after 2000
            }

            int dayOfYear = Integer.parseInt(nicNumber.substring(2, 5)); // getting date from nic number
            // setting gender
            if (dayOfYear > 500) {
                dayOfYear -= 500;
                nic.setGender("Female");
            } else {
                nic.setGender("Male");
            }

            // age and dob calculations
            LocalDate birthDate = calculateBirthDate(year, dayOfYear);
            nic.setBirthDate(birthDate);
            nic.setAge(calculateAge(birthDate));
            

        } catch (NumberFormatException e) {
            throw new InvalidNicException("Invalid Nic Format Detected:" + nicNumber);
        }
    }

    private void parseNewFormatNic(Nic nic) {
        String nicNumber = nic.getNic_no();
        try {
            int year = Integer.parseInt(nicNumber.substring(0, 4));
            int dayOfYear = Integer.parseInt(nicNumber.substring(4, 7));
            // setting gender
            if (dayOfYear > 500) {
                dayOfYear -= 500;
                nic.setGender("Female");
            } else {
                nic.setGender("Male");
            }
            // age and dob calculations
            LocalDate birthDate = calculateBirthDate(year, dayOfYear);
            nic.setBirthDate(birthDate);
            nic.setAge(calculateAge(birthDate));

        } catch (NumberFormatException e) {
            throw new InvalidNicException("Invalid New Nic number"+nicNumber);
        }

    }

    private LocalDate calculateBirthDate(int year, int dayOfYear) {
        //handling leap year logic and parsing 
        if (year % 4 ==0){
            return LocalDate.ofYearDay(year, dayOfYear);
        }
        else{
            if(dayOfYear <60 ){
                return LocalDate.ofYearDay(year, dayOfYear);
                
            }
            else{
                dayOfYear-=1;
                return LocalDate.ofYearDay(year, dayOfYear);
                
            }
        }
    }

    private int calculateAge(LocalDate birthDate) {

        return Period.between(birthDate,LocalDate.now()).getYears();
    }

    //save nic to database
    public Nic saveNic(Nic nic) {
        return nicRepository.save(nic);
    }

    // Get all NIC records from the database
    public List<Nic> getAllNics() {
        return nicRepository.findAll();
    }

     // Get a specific NIC record by NIC number
     public Nic getNicById(String nic_no) {
        return nicRepository.findById(nic_no)
            .orElseThrow(() -> new InvalidNicException("NIC not found"));
    }

    // Delete a NIC record by NIC number
    public void deleteNicById(String nic_no) {
        nicRepository.deleteById(nic_no);
    }

    /*
     * private void identifyGender(int dayOfYear,Nic nic){
     * 
     * if(dayOfYear> 500){
     * dayOfYear-= 500;
     * nic.setGender("Female");
     * }
     * else{
     * nic.setGender("Male");
     * }
     * }
     * 
     */

}
