package com.exemplo.softwarelab.controller;

import com.exemplo.softwarelab.model.Product;
import com.exemplo.softwarelab.service.ProductService;
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

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void createProduct() throws Exception {
        Product p = new Product();
        p.setId(1L);
        p.setName("Produto");
        Mockito.when(productService.createProduct(any(Product.class))).thenReturn(p);
        mockMvc.perform(post("/api/products")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"Produto\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.name").value("Produto"));
    }

    @Test
    void getProductById_Found() throws Exception {
        Product p = new Product();
        p.setId(2L);
        p.setName("X");
        Mockito.when(productService.getProductById(2L)).thenReturn(Optional.of(p));
        mockMvc.perform(get("/api/products/2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(2))
               .andExpect(jsonPath("$.name").value("X"));
    }

    @Test
    void getProductById_NotFound() throws Exception {
        Mockito.when(productService.getProductById(999L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/products/999"))
               .andExpect(status().isNotFound());
    }

    @Test
    void updateProduct_Success() throws Exception {
        Product p = new Product();
        p.setId(3L);
        p.setName("Y");
        Mockito.when(productService.updateProduct(eq(3L), any(Product.class))).thenReturn(p);
        mockMvc.perform(put("/api/products/3")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"Y\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(3))
               .andExpect(jsonPath("$.name").value("Y"));
    }

    @Test
    void updateProduct_NotFound() throws Exception {
        Mockito.when(productService.updateProduct(eq(3L), any(Product.class)))
               .thenThrow(new RuntimeException());
        mockMvc.perform(put("/api/products/3")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"Y\"}"))
               .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_Success() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(4L);
        mockMvc.perform(delete("/api/products/4"))
               .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_NotFound() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(productService).deleteProduct(4L);
        mockMvc.perform(delete("/api/products/4"))
               .andExpect(status().isNotFound());
    }

    @Test
    void getAllProducts() throws Exception {
        Product p = new Product();
        p.setId(5L);
        p.setName("Z");
        Mockito.when(productService.getAllProducts()).thenReturn(Collections.singletonList(p));
        mockMvc.perform(get("/api/products"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(5))
               .andExpect(jsonPath("$[0].name").value("Z"));
    }
}
