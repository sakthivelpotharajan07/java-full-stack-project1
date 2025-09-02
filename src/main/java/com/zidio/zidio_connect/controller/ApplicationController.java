package com.zidio.zidio_connect.controller;

import com.zidio.zidio_connect.model.Applications;
import com.zidio.zidio_connect.model.Job;
import com.zidio.zidio_connect.model.User;
import com.zidio.zidio_connect.repository.ApplicationsRepository;
import com.zidio.zidio_connect.repository.JobRepository;
import com.zidio.zidio_connect.repository.UserRepository;

// Add these imports for logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    // Define a logger for this class
    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private ApplicationsRepository applicationsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @PostMapping
    public Applications applyJob(@RequestBody Applications app) {
        User student = userRepository.findById(app.getStudent().getUserId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Job job = jobRepository.findById(app.getJob().getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        app.setStudent(student);
        app.setJob(job);
        app.setAppliedDate(LocalDateTime.now());
        app.setStatus("Applied");
        return applicationsRepository.save(app);
    }

    @GetMapping("/user/{userId}")
    public List<Applications> getApplicationsByStudent(@PathVariable Long userId) {
        // --- LOGGING FOR STUDENT PAGE ---
        logger.info("--- Fetching applications for student ID: {} ---", userId);
        List<Applications> applications = applicationsRepository.findByStudentIdWithDetails(userId);
        logger.info("Found {} applications for student.", applications.size());
        if (!applications.isEmpty()) {
            // This log will force Java to access the job title. If there's an error, it will happen here.
            logger.info("First application's job title: {}", applications.get(0).getJob().getTitle());
        }
        logger.info("-------------------------------------------------");
        return applications;
    }

    @GetMapping("/job/{jobId}")
    public List<Applications> getApplicationsByJob(@PathVariable Long jobId) {
        // --- LOGGING FOR RECRUITER PAGE ---
        logger.info("--- Fetching applications for job ID: {} ---", jobId);
        List<Applications> applications = applicationsRepository.findByJobIdWithDetails(jobId);
        logger.info("Found {} applications for this job.", applications.size());
        if (!applications.isEmpty()) {
            // These logs will force Java to access the student name and resume link.
            logger.info("First applicant's name: {}", applications.get(0).getStudent().getName());
            String resumeLink = "No Profile or Resume Link";
            if (applications.get(0).getStudent().getProfile() != null) {
                resumeLink = applications.get(0).getStudent().getProfile().getResumeLink();
            }
            logger.info("First applicant's resume link: {}", resumeLink);
        }
        logger.info("-------------------------------------------------");
        return applications;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Object> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Applications app = applicationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        app.setStatus(status);
        applicationsRepository.save(app);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{id}")
    public void deleteApplication(@PathVariable Long id) {
        applicationsRepository.deleteById(id);
    }
}