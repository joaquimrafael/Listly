package com.exemplo.softwarelab.controller;

import com.exemplo.softwarelab.model.Item;
import com.exemplo.softwarelab.service.ItemService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    void addItemToList() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setQuantity(5);
        Mockito.when(itemService.addItemToList(2L, 3L, 5)).thenReturn(item);
        mockMvc.perform(post("/api/items")
               .param("listId", "2")
               .param("productId", "3")
               .param("quantity", "5"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void removeItemFromList_Success() throws Exception {
        Mockito.doNothing().when(itemService).removeItemFromList(2L, 3L);
        mockMvc.perform(delete("/api/items")
               .param("listId", "2")
               .param("productId", "3"))
               .andExpect(status().isNoContent());
    }

    @Test
    void removeItemFromList_NotFound() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(itemService).removeItemFromList(2L, 3L);
        mockMvc.perform(delete("/api/items")
               .param("listId", "2")
               .param("productId", "3"))
               .andExpect(status().isNotFound());
    }

    @Test
    void updateItemQuantity_Success() throws Exception {
        Item updated = new Item();
        updated.setId(1L);
        updated.setQuantity(10);
        Mockito.when(itemService.updateItemQuantity(2L, 3L, 10)).thenReturn(updated);
        mockMvc.perform(put("/api/items")
               .param("listId", "2")
               .param("productId", "3")
               .param("quantity", "10"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void updateItemQuantity_NotFound() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(itemService).updateItemQuantity(2L, 3L, 10);
        mockMvc.perform(put("/api/items")
               .param("listId", "2")
               .param("productId", "3")
               .param("quantity", "10"))
               .andExpect(status().isNotFound());
    }

    @Test
    void getAllListItems_Success() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setQuantity(5);
        Mockito.when(itemService.getAllListItems(2L)).thenReturn(Collections.singletonList(item));
        mockMvc.perform(get("/api/items")
               .param("listId", "2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1))
               .andExpect(jsonPath("$[0].quantity").value(5));
    }

    @Test
    void getAllListItems_NotFound() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(itemService).getAllListItems(2L);
        mockMvc.perform(get("/api/items")
               .param("listId", "2"))
               .andExpect(status().isNotFound());
    }
}
