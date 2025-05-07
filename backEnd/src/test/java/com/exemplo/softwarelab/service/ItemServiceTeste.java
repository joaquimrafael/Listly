package com.exemplo.softwarelab.service;

import com.exemplo.softwarelab.model.Item;
import com.exemplo.softwarelab.repository.ItemRepository;
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
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private long listId = 2L;
    private long productId = 3L;
    private long id = listId * 1000 + productId;
    private Item sample;

    @BeforeEach
    void setup() {
        sample = new Item(listId, productId, 5);
    }

    @Test
    void addItemToList_deveSalvarERetornar() {
        when(itemRepository.save(any(Item.class))).thenReturn(sample);
        var result = itemService.addItemToList(listId, productId, 5);
        assertThat(result).isEqualTo(sample);
    }

    @Test
    void getAllListItems_deveRetornarLista() {
        when(itemRepository.findByListId(listId))
            .thenReturn(Collections.singletonList(sample));
        var result = itemService.getAllListItems(listId);
        assertThat(result).containsExactly(sample);
    }

    @Test
    void removeItemFromList_Sucesso() {
        when(itemRepository.findById(id))
            .thenReturn(Optional.of(sample));
        doNothing().when(itemRepository).deleteById(id);
        itemService.removeItemFromList(listId, productId);
        verify(itemRepository).deleteById(id);
    }

    @Test
    void removeItemFromList_NotFound() {
        when(itemRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.removeItemFromList(listId, productId))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateItemQuantity_Sucesso() {
        when(itemRepository.findById(id))
            .thenReturn(Optional.of(sample));
        var updated = new Item(listId, productId, 10);
        when(itemRepository.save(any(Item.class))).thenReturn(updated);
        var result = itemService.updateItemQuantity(listId, productId, 10);
        assertThat(result.getQuantity()).isEqualTo(10);
    }

    @Test
    void updateItemQuantity_NotFound() {
        when(itemRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.updateItemQuantity(listId, productId, 10))
            .isInstanceOf(RuntimeException.class);
    }
}
