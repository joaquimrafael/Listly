package com.exemplo.softwarelab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exemplo.softwarelab.model.Profile;
import com.exemplo.softwarelab.repository.ProfileRepository;

import java.util.Optional;

@Service
public class ProfileService {
    
    @Autowired
    private ProfileRepository profileRepository; 

    public Profile createProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public Profile updateProfile(Long id, Profile updatedProfile) {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado com ID: " + id));
        existingProfile.setName(updatedProfile.getName());
        existingProfile.setEmail(updatedProfile.getEmail());
        existingProfile.setPassword(updatedProfile.getPassword());
        return profileRepository.save(existingProfile);
    }

    public void deleteProfile(Long id) {
        if (profileRepository.existsById(id)) {
            profileRepository.deleteById(id);
        } else {
            throw new RuntimeException("Perfil não encontrado com ID: " + id);
        }
    }
}