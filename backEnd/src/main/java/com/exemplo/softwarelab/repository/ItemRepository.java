package com.exemplo.softwarelab.repository;

import com.exemplo.softwarelab.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByListId(long listId);
}
