package com.example.nic_management_service.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="nic_store")
public class NicM {

    @Id
    @Column(name="nic_no", nullable= false)
    private String nic_no;

    @Column (name="Gender")
    private String gender;

    @Column (name="file_name")
    private String fileName;

    @Column (name="age")
    private int age;

    @Column (name="birth_date")
    private LocalDate birthDate;

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

    public void setFileName(String fileName){
        this.fileName = fileName ;
    }

    public String getFileName(){
        return fileName;
    }
    
}
