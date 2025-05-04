package com.exemplo.softwarelab.repository;

import com.exemplo.softwarelab.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>{}
