package com.zidio.zidio_connect.controller;

import com.zidio.zidio_connect.model.Job;
import com.zidio.zidio_connect.model.User;
import com.zidio.zidio_connect.repository.JobRepository;
import com.zidio.zidio_connect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public Job createJob(@RequestBody Job job) {
        User recruiter = userRepository.findById(job.getRecruiter().getUserId())
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        job.setRecruiter(recruiter);
        job.setPostedDate(LocalDate.now());
        job.setStatus("open");
        return jobRepository.save(job);
    }

    @GetMapping
    public List<Job> getAllJobs() {
        // FIX: Call the new method instead of findAll()
        return jobRepository.findAllWithRecruiter();
    }

    @GetMapping("/recruiter/{recruiterId}")
    public List<Job> getJobsByRecruiter(@PathVariable Long recruiterId) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        return jobRepository.findByRecruiter(recruiter);
    }

    @DeleteMapping("/{jobId}")
    public void deleteJob(@PathVariable Long jobId) {
        jobRepository.deleteById(jobId);
    }
}