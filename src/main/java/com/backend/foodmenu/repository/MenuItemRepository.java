package com.backend.foodmenu.repository;

import com.backend.foodmenu.entity.MenuCategory;
import com.backend.foodmenu.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    boolean existsByNameIgnoreCaseAndRestaurant_Id(String name, Long restaurantId);

    Optional<MenuItem> findByIdAndRestaurant_Id(Long id, Long restaurantId);

    @Query("""
        SELECT m FROM MenuItem m
        WHERE m.restaurant.id = :restaurantId
            AND (:category IS NULL OR m.category = :category)
            AND (:vegetarian IS NULL OR m.vegetarian = :vegetarian)
            AND (:maxPrice IS NULL OR m.price <= :maxPrice)
    """)
    Page<MenuItem> search(
            @Param("restaurantId") Long restaurantId,
            @Param("category")MenuCategory category,
            @Param("vegetarian") Boolean vegetarian,
            @Param("maxPrice")BigDecimal maxPrice,
            Pageable pageable
            );

    @Modifying
    @Query("DELETE FROM MenuItem m WHERE m.restaurant.id = :restaurantId")
    void deleteAllByRestaurantId(@Param("restaurantId") Long restaurantId);
}
