package com.example.nic_validation_service.repository;

import com.example.nic_validation_service.model.Nic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NicRepository extends JpaRepository<Nic, String> {
    // You can define custom query methods here if needed
}