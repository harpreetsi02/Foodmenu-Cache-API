package com.backend.foodmenu.dto.response;

import com.backend.foodmenu.entity.City;
import com.backend.foodmenu.entity.Cuisine;

import java.util.Set;

public record RestaurantResponse(
        Long id,
        String name,
        String address,
        Double rating,
        CityResponse city,
        Set<CuisineResponse> cuisines
) {
}
