package com.exemplo.softwarelab.service;

import com.exemplo.softwarelab.model.Profile;
import com.exemplo.softwarelab.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Profile profile = profileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
        profile.setName(updatedProfile.getName());
        profile.setEmail(updatedProfile.getEmail());
        return profileRepository.save(profile);
    }

    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new RuntimeException("Perfil não encontrado");
        }
        profileRepository.deleteById(id);
    }
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }
}
