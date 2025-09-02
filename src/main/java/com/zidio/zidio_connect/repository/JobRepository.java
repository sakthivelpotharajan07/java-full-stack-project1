package com.zidio.zidio_connect.repository;

import com.zidio.zidio_connect.model.Job;
import com.zidio.zidio_connect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import this
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByRecruiter(User recruiter);

    // FIX: Add this method to fetch jobs and their recruiters together
    @Query("SELECT j FROM Job j JOIN FETCH j.recruiter")
    List<Job> findAllWithRecruiter();
}