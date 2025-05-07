package com.exemplo.softwarelab.service;

import com.exemplo.softwarelab.model.Product;
import com.exemplo.softwarelab.repository.ProductRepository;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sample;

    @BeforeEach
    void setup() {
        sample = new Product();
        sample.setId(1L);
        sample.setName("Teste");
    }

    @Test
    void createProduct_deveSalvarERetornar() {
        when(productRepository.save(sample)).thenReturn(sample);
        var result = productService.createProduct(sample);
        assertThat(result).isEqualTo(sample);
        verify(productRepository).save(sample);
    }

    @Test
    void getAllProducts_deveRetornarLista() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(sample));
        var result = productService.getAllProducts();
        assertThat(result).containsExactly(sample);
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_Found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sample));
        var result = productService.getProductById(1L);
        assertThat(result).contains(sample);
    }

    @Test
    void getProductById_NotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        var result = productService.getProductById(2L);
        assertThat(result).isEmpty();
    }

    @Test
    void updateProduct_Sucesso() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sample));
        var updated = new Product();
        updated.setName("Novo");
        when(productRepository.save(any(Product.class))).thenReturn(updated);
        var result = productService.updateProduct(1L, updated);
        assertThat(result.getName()).isEqualTo("Novo");
    }

    @Test
    void updateProduct_NotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productService.updateProduct(2L, sample))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("não encontrado");
    }

    @Test
    void deleteProduct_Sucesso() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);
        productService.deleteProduct(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_NotFound() {
        when(productRepository.existsById(2L)).thenReturn(false);
        assertThatThrownBy(() -> productService.deleteProduct(2L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("não encontrado");
    }
}
