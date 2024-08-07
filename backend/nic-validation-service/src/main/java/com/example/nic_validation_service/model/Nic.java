package com.example.nic_validation_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;
//import java.time.Period;

@Entity //To create objcts
public class Nic {
    @Id
    
    private String nic_no; //primary Key
    private String gender;
    private LocalDate birthDate;
    private int age;

    // Getters and Setters

    public void setNic_no(String nic_no){
        this.nic_no = nic_no; 
    }
    public String getNic_no(){
        return nic_no;
    }

    public void setGender(String gender){
        this.gender = gender ;
    }

    public String getGender(){
        return gender;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;

    }
    public LocalDate getBirthDate(){
        return birthDate;
    }

    public void setAge(int age){
        this.age = age;
    }

    public int getAge(){
        return age;
    }
}
