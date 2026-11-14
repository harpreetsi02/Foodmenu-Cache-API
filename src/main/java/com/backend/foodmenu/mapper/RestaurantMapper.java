package com.backend.foodmenu.mapper;

import com.backend.foodmenu.dto.request.RestaurantRequest;
import com.backend.foodmenu.dto.response.CuisineResponse;
import com.backend.foodmenu.dto.response.RestaurantResponse;
import com.backend.foodmenu.entity.City;
import com.backend.foodmenu.entity.Cuisine;
import com.backend.foodmenu.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper {

    private final CityMapper cityMapper;
    private final CuisineMapper cuisineMapper;

    public RestaurantMapper(CityMapper cityMapper, CuisineMapper cuisineMapper){
        this.cityMapper = cityMapper;
        this.cuisineMapper = cuisineMapper;
    }

    public RestaurantResponse toResponse(Restaurant restaurant){

        Set<CuisineResponse> cuisines = restaurant.getCuisines()
                .stream()
                .map(cuisineMapper::toResponse)
                .collect(Collectors.toSet());

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getRating(),
                cityMapper.toResponse(restaurant.getCity()),
                cuisines
        );
    }

    public Restaurant toEntity(RestaurantRequest request, City city, Set<Cuisine> cuisines){

        return new Restaurant(
                request.name(),
                request.address(),
                request.rating(),
                city,
                cuisines
        );
    }
}
