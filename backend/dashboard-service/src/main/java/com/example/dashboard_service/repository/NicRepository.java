package com.example.dashboard_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dashboard_service.model.NicRecord;

@Repository
public interface NicRepository extends JpaRepository<NicRecord, String> {
    @Query("SELECT COUNT(n) FROM NicRecord n")
    long countTotalRecords();

    @Query("SELECT COUNT(n) FROM NicRecord n WHERE n.gender = 'Male'")
    long countMaleUsers();

    @Query("SELECT COUNT(n) FROM NicRecord n WHERE n.gender = 'Female'")
    long countFemaleUsers();

    @Query("SELECT n.fileName, COUNT(n) FROM NicRecord n GROUP BY n.fileName")
    List<Object[]> countRecordsByFileName();
}
