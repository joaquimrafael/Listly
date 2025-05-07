package com.exemplo.softwarelab.service;

import com.exemplo.softwarelab.model.Item;
import com.exemplo.softwarelab.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Item addItemToList(long listId, long productId, int quantity) {
        Item item = new Item(listId, productId, quantity);
        return itemRepository.save(item);
    }

    public List<Item> getAllListItems(long listId) {
        return itemRepository.findByListId(listId);
    }

    public void removeItemFromList(long listId, long productId) {
        long id = listId * 1000 + productId;
        itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Item não encontrado na lista com ID: " + listId +
                " e produto com ID: " + productId));
        itemRepository.deleteById(id);
    }

    public Item updateItemQuantity(long listId, long productId, int quantity) {
        long id = listId * 1000 + productId;
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Item não encontrado na lista com ID: " + listId +
                " e produto com ID: " + productId));
        item.setQuantity(quantity);
        return itemRepository.save(item);
    }
}
