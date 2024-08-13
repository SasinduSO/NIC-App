package com.example.nic_management_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.nic_management_service.model.NicM;

@Repository
public interface NicMRepository extends JpaRepository<NicM, String> {
    
    List <NicM> findByFileName(String fileName);
}
