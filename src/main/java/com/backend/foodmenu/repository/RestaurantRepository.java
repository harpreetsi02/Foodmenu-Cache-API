package com.backend.foodmenu.repository;

import com.backend.foodmenu.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    boolean existsByNameIgnoreCaseAndCity_Id(String name, Long cityId);
}
