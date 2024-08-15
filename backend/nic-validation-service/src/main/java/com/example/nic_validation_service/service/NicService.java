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

@Service // for creating objects
public class NicService {

    // API Passers
    private final RestTemplate restTemplate;

    // private static final String USER_M_URL ="http://localhost:8081/api/nicM";

    @Autowired
    public NicService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // testing
    public String getStatusFromManagementService() {
        String managementServiceUrl = "http://localhost:8081/api/status";
        return restTemplate.getForObject(managementServiceUrl, String.class);
    }

    // send valid nics using rest apis
    public void sendValidNic(List<Nic> validNics) {

        String saveNicUrl = "http://localhost:8081/api/nicM/save";
        restTemplate.postForObject(saveNicUrl, validNics, Void.class);
    }

    // send invalid nics using rest apis
    public void sendInvalidNic(List<InvalidNic> invalidNics) {

        String saveNicUrl = "http://localhost:8081/api/nicM/InvalidSave";
        restTemplate.postForObject(saveNicUrl, invalidNics, Void.class);
    }

    // SQL passers
    @Autowired
    private NicRepository nicRepository;
    @Autowired
    private InvalidRepository invalidRepository;

    // Constructor
    public NicService(NicRepository nicRepository, InvalidRepository invalidRepository, RestTemplate restTemplate) {
        this.nicRepository = nicRepository;
        this.invalidRepository = invalidRepository;
        this.restTemplate = restTemplate;
    }

    // To get and return list of nic numbers from csv
    public List<Nic> parseNicsFromCsv(List<String> nicNumbers, String filename) {

        List<Nic> nics = new ArrayList<>(); // creates a new list to hold coorectnic numbers
        List<String> errmsgs = new ArrayList<>();
        List<InvalidNic> iNics = new ArrayList<>();

        // reading csv(now an array) until end of array
        for (String nicNumber : nicNumbers) {
            try {

                Nic nic = new Nic(); // new object for each iteration for valid nic

                nic.setNic_no(nicNumber);
                nic.setFileName(filename); // Set the filename

                switch (nicNumber.length()) {
                    case 10 -> parseOldFormatNic(nic); // passing nic to old nicparser
                    case 12 -> parseNewFormatNic(nic); // passing nic to new nicparser
                    default -> throw new InvalidNicException("This is an Invalid NIC: " + nicNumber);
                }
                nics.add(nic); // adding to list of objects

            } catch (InvalidNicException e) {
                InvalidNic inic = new InvalidNic(); // OBJECT FOR INVALID
                inic.setNic_no(nicNumber);
                inic.setFileName(filename);
                iNics.add(inic);
                errmsgs.add(e.getMessage());
                System.err.println("Error in format of ID:" + nicNumber + "--" + e.getMessage());
            }
        }
        DisplayInvalidNic(errmsgs); // due to inability to return two values from one classs
        sendValidNic(nics);
        sendInvalidNic(iNics);

        return nics; // return the lust
        // return errmsgs;

    }

    public List<String> DisplayInvalidNic(List<String> errmsgs) {
        return errmsgs;
    }

    // abstraction methids for parsing
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

    // parsing new
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
            throw new InvalidNicException("Invalid New Nic number" + nicNumber);
        }

    }

    // dob logic and calculatioon
    private LocalDate calculateBirthDate(int year, int dayOfYear) {
        // handling leap year logic and parsing
        if (year % 4 == 0) {
            return LocalDate.ofYearDay(year, dayOfYear);
        } else {
            if (dayOfYear < 60) {
                return LocalDate.ofYearDay(year, dayOfYear);

            } else {
                dayOfYear -= 1;
                return LocalDate.ofYearDay(year, dayOfYear);

            }
        }
    }

    // age calculator
    private int calculateAge(LocalDate birthDate) {

        return Period.between(birthDate, LocalDate.now()).getYears();
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

/*
 * public void sendValidNics( List<Nic> validNics){
 * for(Nic nic : validNics){
 * HttpHeaders headers = new HttpHeaders();
 * headers.setContentType(MediaType.APPLICATION_JSON);
 * HttpEntity<Nic> requestEntity = new HttpEntity<>(nic, headers);
 * 
 * ResponseEntity<String> response = restTemplate.exchange(
 * USER_M_URL + "/save",
 * HttpMethod.POST,
 * requestEntity,
 * String.class
 * );
 * 
 * if (response.getStatusCode() != HttpStatus.OK) {
 * System.err.println("Failed to send NIC: " + nic.getNic_no());
 * }
 * }
 * }
 * 
 * 
 */