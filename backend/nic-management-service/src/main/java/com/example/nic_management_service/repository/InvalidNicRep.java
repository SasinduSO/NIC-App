package com.example.nic_management_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.nic_management_service.model.InvalidNic;

@Repository
public interface  InvalidNicRep extends JpaRepository<InvalidNic, String> {
    
}
