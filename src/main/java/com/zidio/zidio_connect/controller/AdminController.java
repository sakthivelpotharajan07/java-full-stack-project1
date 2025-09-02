package com.zidio.zidio_connect.controller;

import com.zidio.zidio_connect.model.Applications;
import com.zidio.zidio_connect.model.Job;
import com.zidio.zidio_connect.model.User;
import com.zidio.zidio_connect.repository.ApplicationsRepository;
import com.zidio.zidio_connect.repository.JobRepository;
import com.zidio.zidio_connect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationsRepository applicationsRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

 @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getAllJobs() {
        // FIX: Use the existing method that fetches recruiters
        return ResponseEntity.ok(jobRepository.findAllWithRecruiter());
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return ResponseEntity.ok("Job deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
    }

    @GetMapping("/applications")
    public ResponseEntity<List<Applications>> getAllApplications() {
        // FIX: Use the new method to fetch all application details
        return ResponseEntity.ok(applicationsRepository.findAllWithDetails());
    }

    @DeleteMapping("/applications/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable Long id) {
        if (applicationsRepository.existsById(id)) {
            applicationsRepository.deleteById(id);
            return ResponseEntity.ok("Application deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found");
    }
}