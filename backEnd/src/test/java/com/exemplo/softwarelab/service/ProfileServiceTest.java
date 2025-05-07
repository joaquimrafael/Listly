package com.exemplo.softwarelab.service;

import com.exemplo.softwarelab.model.Profile;
import com.exemplo.softwarelab.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    private Profile sample;

    @BeforeEach
    void setup() {
        sample = new Profile("Nome", "email@example.com", "senha");
        sample.setId(1L);
    }

    @Test
    void createProfile_deveSalvarERetornar() {
        when(profileRepository.save(sample)).thenReturn(sample);
        var result = profileService.createProfile(sample);
        assertThat(result).isEqualTo(sample);
        verify(profileRepository).save(sample);
    }

    @Test
    void getAllProfiles_deveRetornarLista() {
        when(profileRepository.findAll()).thenReturn(Collections.singletonList(sample));
        var result = profileService.getAllProfiles();
        assertThat(result).containsExactly(sample);
        verify(profileRepository).findAll();
    }

    @Test
    void getProfileById_Found() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(sample));
        var result = profileService.getProfileById(1L);
        assertThat(result).contains(sample);
    }

    @Test
    void getProfileById_NotFound() {
        when(profileRepository.findById(2L)).thenReturn(Optional.empty());
        var result = profileService.getProfileById(2L);
        assertThat(result).isEmpty();
    }

    @Test
    void updateProfile_Sucesso() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(sample));
        var updated = new Profile("Outro", "novo@example.com", "nova");
        when(profileRepository.save(any(Profile.class))).thenReturn(updated);
        var result = profileService.updateProfile(1L, updated);
        assertThat(result.getName()).isEqualTo("Outro");
    }

    @Test
    void updateProfile_NotFound() {
        when(profileRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> profileService.updateProfile(2L, sample))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("não encontrado");
    }

    @Test
    void deleteProfile_Sucesso() {
        when(profileRepository.existsById(1L)).thenReturn(true);
        doNothing().when(profileRepository).deleteById(1L);
        profileService.deleteProfile(1L);
        verify(profileRepository).deleteById(1L);
    }

    @Test
    void deleteProfile_NotFound() {
        when(profileRepository.existsById(2L)).thenReturn(false);
        assertThatThrownBy(() -> profileService.deleteProfile(2L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("não encontrado");
    }
}
