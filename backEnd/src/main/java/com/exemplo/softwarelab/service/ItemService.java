package com.exemplo.softwarelab.service;

import com.exemplo.softwarelab.model.Item;
import com.exemplo.softwarelab.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Item addItemToList(long listId, long productId, int quantity) {
        Item item = new Item(listId, productId, quantity);
        return itemRepository.save(item);
    }

    public void removeItemFromList(long listId, long productId) {
        List<Item> itemsToDelete = itemRepository.findAll().stream()
                .filter(item -> item.getListId() == listId && item.getProductId() == productId)
                .toList();
        if (itemsToDelete.isEmpty()) {
            throw new RuntimeException("Item não encontrado na lista com ID: " + listId + " e produto com ID: " + productId);
        }
        itemRepository.deleteAll(itemsToDelete);
    }

    public Item updateItemQuantity(long listId, long productId, int newQuantity) {
        Optional<Item> optionalItem = itemRepository.findAll().stream()
                .filter(item -> item.getListId() == listId && item.getProductId() == productId)
                .findFirst();

        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            item.setQuantity(newQuantity);
            return itemRepository.save(item);
        } else {
            throw new RuntimeException("Item não encontrado na lista com ID: " + listId + " e produto com ID: " + productId);
        }
    }

    public List<Item> getAllListItems(long listId) {
        return itemRepository.findAll().stream()
                .filter(item -> item.getListId() == listId)
                .toList();
    }
}