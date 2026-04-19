package com.example.demo.repository;

import com.example.demo.enums.ApplicationStatus;
import com.example.demo.enums.JobType;
import com.example.demo.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUserId(Long userId);
    List<Application> findByUserIdAndStatus(Long userId, ApplicationStatus status);
    List<Application> findByUserIdAndJobType(Long userId, JobType jobType);
    List<Application> findByUserIdAndCompanyNameContainingIgnoreCase(Long userId, String companyName);
}