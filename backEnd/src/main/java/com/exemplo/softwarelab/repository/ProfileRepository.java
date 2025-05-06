package com.exemplo.softwarelab.repository;

import com.exemplo.softwarelab.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
