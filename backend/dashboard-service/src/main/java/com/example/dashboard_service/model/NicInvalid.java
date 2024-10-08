package com.example.dashboard_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="invalid_store")
public class NicInvalid {
    @Id
    @Column(name="nic_no", nullable = false)
    private String nic_no;

    @Column(name= "file_name")
    private String filename;

    @Column(name= "error_msg")
    private String errorMessage;

    //Getter and setters

    public void setNic_no(String nic_no){
        this.nic_no = nic_no; 
    }
    public String getNic_no(){
        return nic_no;
    }

    public void setFileName(String filename){
        this.filename = filename ;
    }

    public String getFileName(){
        return filename;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    
    }

}
