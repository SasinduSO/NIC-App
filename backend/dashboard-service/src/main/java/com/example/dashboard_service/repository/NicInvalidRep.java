package com.example.dashboard_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dashboard_service.model.NicInvalid;

@Repository
public interface NicInvalidRep extends JpaRepository<NicInvalid, String> {
    @Query("SELECT COUNT(n) FROM NicInvalid n")
    long countTotalInvalid();
    
    @Query("SELECT n.filename, COUNT(n) FROM NicInvalid n GROUP BY n.filename")
    List<Object[]> countInvalidsByFileName();

    

}
