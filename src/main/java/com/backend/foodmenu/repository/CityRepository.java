package com.backend.foodmenu.repository;

import com.backend.foodmenu.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {

    boolean existsByNameIgnoreCase(String name);
}
