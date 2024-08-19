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

    // Query to count records by age ranges
    @Query("SELECT " +
        "CASE " +
        "WHEN age BETWEEN 10 AND 20 THEN '10-20' " +
        "WHEN age BETWEEN 20 AND 30 THEN '20-30' " +
        "WHEN age BETWEEN 30 AND 40 THEN '30-40' " +
        "WHEN age BETWEEN 40 AND 50 THEN '40-50' " +
        "ELSE 'Above 60' END, COUNT(r) " +
        "FROM NicRecord r GROUP BY CASE " +
        "WHEN age BETWEEN 10 AND 20 THEN '10-20' " +
        "WHEN age BETWEEN 20 AND 30 THEN '20-30' " +
        "WHEN age BETWEEN 30 AND 40 THEN '30-40' " +
        "WHEN age BETWEEN 40 AND 50 THEN '40-50' " +
        "ELSE 'Above 60' END")
    List<Object[]> countByAgeRanges();
}
