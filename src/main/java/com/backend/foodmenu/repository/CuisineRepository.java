package com.backend.foodmenu.repository;

import com.backend.foodmenu.entity.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuisineRepository extends JpaRepository<Cuisine, Long> {

    boolean existsByNameIgnoreCase(String name);
}
