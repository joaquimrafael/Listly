package com.exemplo.softwarelab.repository;

import com.exemplo.softwarelab.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
