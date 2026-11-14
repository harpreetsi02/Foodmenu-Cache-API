package com.backend.foodmenu.repository;

import com.backend.foodmenu.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    boolean existsByNameIgnoreCaseAndCity_Id(String name, Long cityId);

    boolean existsByCity_Id(Long cityId);

    @Query("SELECT COUNT(r) > 0 FROM Restaurant r JOIN r.cuisines c WHERE c.id = :cuisineId")
    boolean existsByCuisineId(@Param("cuisineId") Long cuisineId);
}
