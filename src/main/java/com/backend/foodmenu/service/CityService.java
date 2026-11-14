package com.backend.foodmenu.service;

import com.backend.foodmenu.dto.request.CityRequest;
import com.backend.foodmenu.dto.response.CityResponse;
import com.backend.foodmenu.entity.City;
import com.backend.foodmenu.exception.DuplicateResourceException;
import com.backend.foodmenu.exception.ResourceNotFoundException;
import com.backend.foodmenu.mapper.CityMapper;
import com.backend.foodmenu.repository.CityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public CityService(CityRepository cityRepository, CityMapper cityMapper){
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    @Transactional
    public CityResponse createCity(CityRequest request){
        if (cityRepository.existsByNameIgnoreCase(request.name())){
            throw new DuplicateResourceException("City", request.name());
        }

        City city = cityMapper.toEntity(request);
        City savedCity = cityRepository.save(city);
        return cityMapper.toResponse(savedCity);
    }

    @Transactional(readOnly = true)
    public CityResponse getCityById(Long id){
        City city = findCity(id);

        return cityMapper.toResponse(city);
    }

    @Transactional(readOnly = true)
    public List<CityResponse> findAllCities(){

        return cityRepository.findAll()
                .stream()
                .map(cityMapper::toResponse)
                .toList();
    }

    @Transactional
    public CityResponse updateCity(Long id, CityRequest request){
        City city = findCity(id);
        boolean nameChanged = !city.getName().equalsIgnoreCase(request.name());

        if (nameChanged && cityRepository.existsByNameIgnoreCase(request.name())){
            throw new DuplicateResourceException("City", request.name());
        }

        city.setName(request.name());
        city.setState(request.state());

        return cityMapper.toResponse(city);
    }

    @Transactional
    public void deleteCity(Long id){
        cityRepository.delete(findCity(id));
    }

    private City findCity(Long id){
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City", id));
    }
}
