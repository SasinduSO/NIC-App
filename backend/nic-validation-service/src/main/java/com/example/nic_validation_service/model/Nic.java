package com.example.nic_validation_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
//import java.time.Period;

@Entity //points to JPA entity and maps class to atable 
@Table(name = "nic_store")  // Maps this entity to the nic_store table

public class Nic {
    @Id
    @Column(name= "nic_no", length=12, nullable = false )
    private String nic_no; //primary Key

    @Column(name= "Gender" , length=7 )
    private String gender;

    @Column(name= "birthDate")
    private LocalDate birthDate;

    @Column(name = "age")
    private int age;

    @Column(name = "file_name")
    private String fileName;

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
