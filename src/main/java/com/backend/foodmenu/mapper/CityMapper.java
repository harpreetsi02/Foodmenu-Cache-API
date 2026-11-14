package com.backend.foodmenu.mapper;

import com.backend.foodmenu.dto.request.CityRequest;
import com.backend.foodmenu.dto.response.CityResponse;
import com.backend.foodmenu.entity.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    public CityResponse toResponse(City city){
        return new CityResponse(city.getId(), city.getName(), city.getState());
    }

    public City toEntity(CityRequest request){
        return new City(request.name(), request.state());
    }
}
