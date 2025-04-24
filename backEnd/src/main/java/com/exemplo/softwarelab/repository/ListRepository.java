package com.exemplo.softwarelab.repository;

import com.exemplo.softwarelab.model.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<List, Long> {
}