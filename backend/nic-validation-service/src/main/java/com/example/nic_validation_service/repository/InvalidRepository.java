package com.example.nic_validation_service.repository;

import com.example.nic_validation_service.model.InvalidNic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidRepository extends JpaRepository <InvalidNic, String> {
    
}
