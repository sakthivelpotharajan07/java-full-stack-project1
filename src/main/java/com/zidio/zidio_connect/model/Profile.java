package com.zidio.zidio_connect.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @JsonIgnore // 2. ADD THIS ANNOTATION
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"profile", "applications", "jobs", "password"})
    private User user;

    @URL(message = "Please provide a valid URL for the resume link")
    @Column(name = "resume_link")
    private String resumeLink;

    public Profile() {}
    
    // Getters & Setters
    public Long getProfileId() { return profileId; }
    public void setProfileId(Long profileId) { this.profileId = profileId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getResumeLink() { return resumeLink; }
    public void setResumeLink(String resumeLink) { this.resumeLink = resumeLink; }
}