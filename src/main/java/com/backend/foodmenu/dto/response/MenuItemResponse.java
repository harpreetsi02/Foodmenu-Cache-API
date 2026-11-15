package com.backend.foodmenu.dto.response;

import com.backend.foodmenu.entity.MenuCategory;
import com.backend.foodmenu.entity.Restaurant;

import java.math.BigDecimal;

public record MenuItemResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        MenuCategory category,
        boolean vegetarian,
        boolean available,
        Long restaurantId
) {
}
