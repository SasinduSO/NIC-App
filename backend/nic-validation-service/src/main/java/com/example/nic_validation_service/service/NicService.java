package com.example.nic_validation_service.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.nic_validation_service.exception.InvalidNicException;
import com.example.nic_validation_service.model.InvalidNic;
import com.example.nic_validation_service.model.Nic;
import com.example.nic_validation_service.repository.InvalidRepository;
import com.example.nic_validation_service.repository.NicRepository;

//import javax.print.DocFlavor.STRING;

@Service // for creating objects
public class NicService {

    //API Passers
    private final RestTemplate restTemplate;
    @Autowired
    public NicService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getStatusFromManagementService() {
        String managementServiceUrl = "http://localhost:8081/api/status";
        return restTemplate.getForObject(managementServiceUrl, String.class);
    }


    //SQL passers
    private NicRepository nicRepository;
    private InvalidRepository invalidRepository;

    //Constructor
    public NicService(NicRepository nicRepository, InvalidRepository invalidRepository, RestTemplate restTemplate) {
        this.nicRepository = nicRepository;
        this.invalidRepository=invalidRepository;
        this.restTemplate= restTemplate;
    }

    




    // To get and return list of nic numbers from csv
    public List<Nic> parseNicsFromCsv(List<String> nicNumbers, String filename) {

        List<Nic> nics = new ArrayList<>(); // creates a new list to hold coorectnic numbers
        List<String> errmsgs = new ArrayList<>();


        // reading csv(now an array) until end of array
        for (String nicNumber : nicNumbers) {
            try{

            Nic nic = new Nic(); // new object for each iteration
            nic.setNic_no(nicNumber);
            nic.setFileName(filename); // Set the filename


            switch (nicNumber.length()) {
                case 10 -> parseOldFormatNic(nic); // passing nic to old nicparser
                case 12 -> parseNewFormatNic(nic); // passing nic to new nicparser
                default -> throw new InvalidNicException("This is an Invalid NIC: " + nicNumber);
            }
            nics.add(nic); // adding to list of objects
            saveNic( nic);

        }catch(InvalidNicException e){
            saveInvalid(nicNumber,filename);
            errmsgs.add(e.getMessage());
            System.err.println("Error in format of ID:"+ nicNumber + "--"+ e.getMessage());
        }
        }
        DisplayInvalidNic(errmsgs); //due to inability to return two values from one classs
        return nics ; // return the lust
        //return errmsgs;

    }

    public List<String> DisplayInvalidNic(List<String> errmsgs){
        return errmsgs;
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

    //saving invalid nic
    public InvalidNic saveInvalid(String nicNumber, String filename){
        InvalidNic inic = new InvalidNic();
        inic.setNic_no(nicNumber);
        inic.setFileName(filename);
        return invalidRepository.save(inic);
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
