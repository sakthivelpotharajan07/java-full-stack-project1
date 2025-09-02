package com.zidio.zidio_connect.controller;

import com.zidio.zidio_connect.model.Profile;
import com.zidio.zidio_connect.model.User;
import com.zidio.zidio_connect.repository.ProfileRepository;
import com.zidio.zidio_connect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{userId}")
    public Profile saveProfile(@PathVariable Long userId, @RequestBody Profile profileDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return profileRepository.findByUser(user)
            .map(existingProfile -> {
                // Update existing profile
                existingProfile.setResumeLink(profileDetails.getResumeLink());
                return profileRepository.save(existingProfile);
            })
            .orElseGet(() -> {
                // Create new profile
                Profile newProfile = new Profile();
                newProfile.setUser(user);
                newProfile.setResumeLink(profileDetails.getResumeLink());
                return profileRepository.save(newProfile);
            });
    }

    @GetMapping("/{userId}")
    public Profile getProfile(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return profileRepository.findByUser(user)
                .orElse(null); // Return null or throw exception if profile not found
    }
}