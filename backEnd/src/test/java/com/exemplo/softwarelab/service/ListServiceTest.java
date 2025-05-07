package com.exemplo.softwarelab.service;

import com.exemplo.softwarelab.model.List;
import com.exemplo.softwarelab.repository.ListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListServiceTest {

    @Mock
    private ListRepository listRepository;

    @InjectMocks
    private ListService listService;

    private List sample;

    @BeforeEach
    void setup() {
        sample = new List();
        sample.setId(1L);
        sample.setName("Teste");
    }

    @Test
    void deleteList_NotFound() {
        when(listRepository.existsById(2L)).thenReturn(false);
        assertThatThrownBy(() -> listService.deleteList(2L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("não encontrada");
    }

    @Test
    void updateList_NotFound() {
        when(listRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> listService.updateList(2L, sample))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("não encontrada");
    }

    // restante dos testes idem ao original...
}
