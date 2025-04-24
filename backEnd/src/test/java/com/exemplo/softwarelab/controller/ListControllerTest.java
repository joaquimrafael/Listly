package com.exemplo.softwarelab.controller;

import com.exemplo.softwarelab.model.List;
import com.exemplo.softwarelab.repository.ListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ListRepository listRepository;

    @BeforeEach
    public void setUp() {
        // Limpa o repositório antes de cada teste
        listRepository.deleteAll();
    }

    @Test
    public void testCreateList() throws Exception {
        // Testa o endpoint POST /api/lists
        String listJson = "{\"name\":\"Minha Lista\"}";

        mockMvc.perform(post("/api/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(listJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Minha Lista"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void testGetListById() throws Exception {
        // Cria uma lista para testar o GET
        List list = new List("Lista de Teste");
        list = listRepository.save(list);

        mockMvc.perform(get("/api/lists/" + list.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lista de Teste"))
                .andExpect(jsonPath("$.id").value(list.getId()));
    }

    @Test
    public void testUpdateList() throws Exception {
        // Cria uma lista para testar o PUT
        List list = new List("Lista Original");
        list = listRepository.save(list);

        String updatedListJson = "{\"name\":\"Lista Atualizada\"}";

        mockMvc.perform(put("/api/lists/" + list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedListJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lista Atualizada"))
                .andExpect(jsonPath("$.id").value(list.getId()));
    }

    @Test
    public void testDeleteList() throws Exception {
        // Cria uma lista para testar o DELETE
        List list = new List("Lista para Deletar");
        list = listRepository.save(list);

        mockMvc.perform(delete("/api/lists/" + list.getId()))
                .andExpect(status().isNoContent());

        // Verifica se a lista foi deletada
        mockMvc.perform(get("/api/lists/" + list.getId()))
                .andExpect(status().isNotFound());
    }
}