package com.backend.foodmenu.mapper;

import com.backend.foodmenu.dto.request.CuisineRequest;
import com.backend.foodmenu.dto.response.CuisineResponse;
import com.backend.foodmenu.entity.Cuisine;
import org.springframework.stereotype.Component;

@Component
public class CuisineMapper {

    public CuisineResponse toResponse(Cuisine cuisine){
        return new CuisineResponse(cuisine.getId(), cuisine.getName(), cuisine.getDescription());
    }

    public Cuisine toEntity(CuisineRequest request){
        return new Cuisine(request.name(), request.description());
    }
}
