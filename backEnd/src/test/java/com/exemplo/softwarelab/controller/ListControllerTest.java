package com.exemplo.softwarelab.controller;

import com.exemplo.softwarelab.model.List;
import com.exemplo.softwarelab.service.ListService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ListController.class)
class ListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListService listService;

    @Test
    void getAllLists() throws Exception {
        List l = new List();
        l.setId(1L);
        l.setName("Minha Lista");
        Mockito.when(listService.getAllLists()).thenReturn(Collections.singletonList(l));
        mockMvc.perform(get("/api/lists"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1))
               .andExpect(jsonPath("$[0].name").value("Minha Lista"));
    }

    @Test
    void getListById_Found() throws Exception {
        List l = new List();
        l.setId(2L);
        l.setName("Outra");
        Mockito.when(listService.getListById(2L)).thenReturn(Optional.of(l));
        mockMvc.perform(get("/api/lists/2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(2))
               .andExpect(jsonPath("$.name").value("Outra"));
    }

    @Test
    void getListById_NotFound() throws Exception {
        Mockito.when(listService.getListById(999L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/lists/999"))
               .andExpect(status().isNotFound());
    }

    @Test
    void createList() throws Exception {
        List saved = new List();
        saved.setId(10L);
        saved.setName("Nova");
        Mockito.when(listService.createList(Mockito.any(List.class))).thenReturn(saved);
        String json = "{\"name\":\"Nova\"}";
        mockMvc.perform(post("/api/lists")
               .contentType(MediaType.APPLICATION_JSON)
               .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(10))
               .andExpect(jsonPath("$.name").value("Nova"));
    }

    @Test
    void deleteList_Success() throws Exception {
        Mockito.doNothing().when(listService).deleteList(5L);
        mockMvc.perform(delete("/api/lists/5"))
               .andExpect(status().isNoContent());
    }

    @Test
    void deleteList_NotFound() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(listService).deleteList(7L);
        mockMvc.perform(delete("/api/lists/7"))
               .andExpect(status().isNotFound());
    }
}
