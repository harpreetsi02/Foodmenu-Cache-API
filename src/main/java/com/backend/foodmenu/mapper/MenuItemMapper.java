package com.backend.foodmenu.mapper;

import com.backend.foodmenu.dto.request.MenuItemRequest;
import com.backend.foodmenu.dto.response.MenuItemResponse;
import com.backend.foodmenu.entity.MenuItem;
import com.backend.foodmenu.entity.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {

    public MenuItemResponse toResponse(MenuItem menuItem){

        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getCategory(),
                menuItem.isVegetarian(),
                menuItem.isAvailable(),
                menuItem.getRestaurant().getId()
        );
    }

    public MenuItem toEntity(MenuItemRequest request, Restaurant restaurant){

        boolean available = request.available() == null || request.available();
        return new MenuItem(
                request.name(),
                request.description(),
                request.price(),
                request.category(),
                request.vegetarian(),
                available,
                restaurant
        );
    }
}
