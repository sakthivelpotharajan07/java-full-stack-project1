package com.zidio.zidio_connect.repository;

import com.zidio.zidio_connect.model.Profile;
import com.zidio.zidio_connect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);
}