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

@Service
public class NicService {

    private final RestTemplate restTemplate;
    //private final NicRepository nicRepository;
    //private final InvalidRepository invalidRepository;

    // Constructor with dependeency inject
    @Autowired
    public NicService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
      //  this.nicRepository = nicRepository;
      //  this.invalidRepository = invalidRepository;
    }

    // Method to parse NICs froCSV and validate them
    public List<Nic> parseNicsFromCsv(List<String> nicNumbers, String filename) {
        List<Nic> validNics = new ArrayList<>();
        List<InvalidNic> invalidNics = new ArrayList<>();

        for (String nicNumber : nicNumbers) {
            try {
                validateNicFormat(nicNumber); // Validate NIC format

                Nic nic = new Nic();
                nic.setNic_no(nicNumber);
                nic.setFileName(filename);

                // Parse NIC based on its format (old or new)
                switch (nicNumber.length()) {
                    case 10 -> parseOldFormatNic(nic);
                    case 12 -> parseNewFormatNic(nic);
                    default -> throw new InvalidNicException("Invalid NIC length: " + nicNumber);
                }

                validNics.add(nic);
            } catch (InvalidNicException e) {
                // If NIC is invalid, add it to the list of invalid NICs
                //storing of invalids
                InvalidNic invalidNic = new InvalidNic();
                invalidNic.setNic_no(nicNumber);
                invalidNic.setFileName(filename);
                invalidNic.setErrorMessage(e.getMessage()); 
                invalidNics.add(invalidNic);
            }
        }

        // Send valid NICs to the management service
        if (!validNics.isEmpty()) sendValidNic(validNics);
        // Send invalid NICs to the management service
        if (!invalidNics.isEmpty()) sendInvalidNic(invalidNics);

        return validNics;
    }

    // Method to validate the format of a NIC number
    private void validateNicFormat(String nicNumber) {
        switch (nicNumber.length()) {
            case 10 -> {
                if (!nicNumber.matches("\\d{9}[VX]")) {
                    throw new InvalidNicException("Invalid NIC format (old): " + nicNumber);
                }
            }
            case 12 -> {
                if (!nicNumber.matches("\\d{12}")) {
                    throw new InvalidNicException("Invalid NIC format (new): " + nicNumber);
                }
            }
            default -> throw new InvalidNicException("Found Invalid charachters in NIC : " + nicNumber);
        }
    }

    // Method to parse NICs with the old format (10 characters)
    private void parseOldFormatNic(Nic nic) {
        String nicNumber = nic.getNic_no();

        // Updated year logic
        int year;
        int yearVal = Integer.parseInt(nicNumber.substring(0, 2));
        if (yearVal >= 10) {
            year = 1900 + yearVal; // If person born before 2000
        } else {
            year = 2000 + yearVal; // If person born in or after 2000
        }

        int dayOfYear = Integer.parseInt(nicNumber.substring(2, 5));

        // Determine gender based on the day of the year
        if (dayOfYear <= 366) {
            nic.setGender("Male");
        } else if (dayOfYear >= 501 && dayOfYear <= 866) {
            nic.setGender("Female");
            dayOfYear -= 500;
        } else {
            throw new InvalidNicException("Invalid Number for Date of birth: " + nicNumber);
        }

        validateAge(year); // Validate the calculated age

        LocalDate birthDate = LocalDate.ofYearDay(year, dayOfYear);
        nic.setBirthDate(birthDate);
        nic.setAge(calculateAge(birthDate));
    }

    // Method to parse NICs with the new format (12 characters)
    private void parseNewFormatNic(Nic nic) {
        String nicNumber = nic.getNic_no();
        int year = Integer.parseInt(nicNumber.substring(0, 4));
        int dayOfYear = Integer.parseInt(nicNumber.substring(4, 7));

        // Determine gender based on the day of the year
        if (dayOfYear <= 366) {
            nic.setGender("Male");
        } else if (dayOfYear >= 501 && dayOfYear <= 866) {
            nic.setGender("Female");
            dayOfYear -= 500;
        } else {
            throw new InvalidNicException("Invalid day of year for gender in NIC: " + nicNumber);
        }

        validateAge(year); // Validate the calculated age

        LocalDate birthDate = LocalDate.ofYearDay(year, dayOfYear);
        nic.setBirthDate(birthDate);
        nic.setAge(calculateAge(birthDate));
    }

    // Method to validate the age derived from the NIC
    private void validateAge(int year) {
        int currentYear = LocalDate.now().getYear();
        int age = currentYear - year;

        if (age < 16) {
            throw new InvalidNicException("Ages below 16 are not issued NICs");
        }
    }

    // Method to calculate the age based on birth date
    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // Method to send valid NICs to the management service
    public void sendValidNic(List<Nic> validNics) {
        String saveNicUrl = "http://localhost:8081/api/nicM/save";
        restTemplate.postForObject(saveNicUrl, validNics, Void.class);
    }

    // Method to send invalid NICs to the management service
    public void sendInvalidNic(List<InvalidNic> invalidNics) {
        String saveNicUrl = "http://localhost:8081/api/nicM/InvalidSave";
        restTemplate.postForObject(saveNicUrl, invalidNics, Void.class);
    }

    // Method to get the status from the management service
    public String getStatusFromManagementService() {
        String managementServiceUrl = "http://localhost:8081/api/status";
        return restTemplate.getForObject(managementServiceUrl, String.class);
    }
}
