package com.zidio.zidio_connect.repository;

import com.zidio.zidio_connect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Import Optional

public interface UserRepository extends JpaRepository<User, Long> {

    // FIX: Add this method declaration
    Optional<User> findByEmail(String email);
}