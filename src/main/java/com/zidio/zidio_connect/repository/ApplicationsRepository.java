package com.zidio.zidio_connect.repository;

import com.zidio.zidio_connect.model.Applications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationsRepository extends JpaRepository<Applications, Long> {

    /**
     * Query for the Recruiter's view of applications for a specific job.
     * This fetches the Application, its Student, the Student's Profile, and the Job.
     */
    @Query("SELECT a FROM Applications a " +
           "JOIN FETCH a.student s " +
           "JOIN FETCH a.job j " +
           "LEFT JOIN FETCH s.profile " +
           "WHERE j.id = :jobId")
    List<Applications> findByJobIdWithDetails(@Param("jobId") Long jobId);

    /**
     * Query for the Student's "My Applications" view.
     * This now fetches the Application, its Job, AND its Student.
     */
    @Query("SELECT a FROM Applications a JOIN FETCH a.job j JOIN FETCH a.student s WHERE s.id = :studentId")
    List<Applications> findByStudentIdWithDetails(@Param("studentId") Long studentId);

    @Query("SELECT a FROM Applications a JOIN FETCH a.student s JOIN FETCH a.job j")
    List<Applications> findAllWithDetails();
}