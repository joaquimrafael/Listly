package com.exemplo.softwarelab.controller;

import com.exemplo.softwarelab.model.Profile;
import com.exemplo.softwarelab.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @Test
    void createProfile() throws Exception {
        Profile p = new Profile("Teste", "e@x.com", "pass");
        p.setId(1L);
        Mockito.when(profileService.createProfile(any(Profile.class))).thenReturn(p);
        mockMvc.perform(post("/api/profiles")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"Teste\",\"email\":\"e@x.com\",\"password\":\"pass\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.name").value("Teste"));
    }

    @Test
    void getProfileById_Found() throws Exception {
        Profile p = new Profile("A","a@b","123");
        p.setId(2L);
        Mockito.when(profileService.getProfileById(2L)).thenReturn(Optional.of(p));
        mockMvc.perform(get("/api/profiles/2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(2))
               .andExpect(jsonPath("$.name").value("A"));
    }

    @Test
    void getProfileById_NotFound() throws Exception {
        Mockito.when(profileService.getProfileById(999L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/profiles/999"))
               .andExpect(status().isNotFound());
    }

    @Test
    void updateProfile_Success() throws Exception {
        Profile p = new Profile("B","b@c","456");
        p.setId(3L);
        Mockito.when(profileService.updateProfile(eq(3L), any(Profile.class))).thenReturn(p);
        mockMvc.perform(put("/api/profiles/3")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"B\",\"email\":\"b@c\",\"password\":\"456\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(3))
               .andExpect(jsonPath("$.name").value("B"));
    }

    @Test
    void updateProfile_NotFound() throws Exception {
        Mockito.when(profileService.updateProfile(eq(3L), any(Profile.class)))
               .thenThrow(new RuntimeException());
        mockMvc.perform(put("/api/profiles/3")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"B\"}"))
               .andExpect(status().isNotFound());
    }

    @Test
    void deleteProfile_Success() throws Exception {
        Mockito.doNothing().when(profileService).deleteProfile(4L);
        mockMvc.perform(delete("/api/profiles/4"))
               .andExpect(status().isNoContent());
    }

    @Test
    void deleteProfile_NotFound() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(profileService).deleteProfile(4L);
        mockMvc.perform(delete("/api/profiles/4"))
               .andExpect(status().isNotFound());
    }

    @Test
    void getAllProfiles() throws Exception {
        Profile p = new Profile("C","c@d","789");
        p.setId(5L);
        Mockito.when(profileService.getAllProfiles()).thenReturn(Collections.singletonList(p));
        mockMvc.perform(get("/api/profiles"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(5))
               .andExpect(jsonPath("$[0].name").value("C"));
    }
}
