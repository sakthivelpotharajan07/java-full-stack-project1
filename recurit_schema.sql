DROP DATABASE zidio_connect;
CREATE DATABASE zidio_connect;
USE zidio_connect;


-- USERS TABLE (Students, Recruiters, Admins)
CREATE TABLE Users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('student','recruiter','admin') NOT NULL,
    status ENUM('active','blocked') DEFAULT 'active'
);


-- JOBS TABLE (Posted by Recruiters)
CREATE TABLE Jobs (
    job_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    location VARCHAR(100),
    job_type VARCHAR(50),
    status ENUM('Open','Closed') DEFAULT 'Open',
    posted_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    recruiter_id BIGINT NOT NULL,
    FOREIGN KEY (recruiter_id) REFERENCES Users(user_id) ON DELETE CASCADE
);


-- PROFILES TABLE (Students' profiles/resumes)
CREATE TABLE Profiles (
    profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    resume_link VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);


-- APPLICATIONS TABLE (Students apply for Jobs)
CREATE TABLE applications (
    application_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    applied_date DATETIME,
    status VARCHAR(50),
    job_id BIGINT,
    student_id BIGINT,
    FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE
);


UPDATE users SET role = 'ROLE_ADMIN' WHERE email = 'admin@zidio.com';

SHOW TABLES;

