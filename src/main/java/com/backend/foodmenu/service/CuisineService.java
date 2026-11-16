package com.backend.foodmenu.service;

import com.backend.foodmenu.config.CacheNames;
import com.backend.foodmenu.dto.request.CuisineRequest;
import com.backend.foodmenu.dto.response.CuisineResponse;
import com.backend.foodmenu.entity.Cuisine;
import com.backend.foodmenu.exception.DuplicateResourceException;
import com.backend.foodmenu.exception.ResourceInUseException;
import com.backend.foodmenu.exception.ResourceNotFoundException;
import com.backend.foodmenu.mapper.CuisineMapper;
import com.backend.foodmenu.repository.CuisineRepository;
import com.backend.foodmenu.repository.RestaurantRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CuisineService {

    private final CuisineRepository cuisineRepository;
    private final RestaurantRepository restaurantRepository;
    private final CuisineMapper cuisineMapper;

    public CuisineService(CuisineRepository cuisineRepository, RestaurantRepository restaurantRepository, CuisineMapper cuisineMapper){
        this.cuisineRepository = cuisineRepository;
        this.restaurantRepository = restaurantRepository;
        this.cuisineMapper = cuisineMapper;
    }

    @CacheEvict(cacheNames = CacheNames.CUISINES, key = "'all'")
    @Transactional
    public CuisineResponse createCuisine(CuisineRequest request){
        if (cuisineRepository.existsByNameIgnoreCase(request.name())){
            throw new DuplicateResourceException("Cuisine", request.name());
        }

        Cuisine cuisine = cuisineMapper.toEntity(request);
        Cuisine savedCuisine = cuisineRepository.save(cuisine);
        return cuisineMapper.toResponse(savedCuisine);
    }

    @Cacheable(cacheNames = CacheNames.CUISINES, key = "#id")
    @Transactional(readOnly = true)
    public CuisineResponse getCuisineById(Long id){
        return cuisineMapper.toResponse(findCuisine(id));
    }

    @Cacheable(cacheNames = CacheNames.CUISINES, key = "'all'")
    @Transactional(readOnly = true)
    public List<CuisineResponse> getAllCuisines(){

        return cuisineRepository.findAll()
                .stream()
                .map(cuisineMapper::toResponse)
                .toList();
    }

    @Caching(
            put = @CachePut(cacheNames = CacheNames.CUISINES, key = "#id"),
            evict = {
                    @CacheEvict(cacheNames = CacheNames.CUISINES, key = "'all'"),
                    @CacheEvict(cacheNames = CacheNames.RESTAURANTS, allEntries = true)
            }
    )
    @Transactional
    public CuisineResponse updateCuisine(Long id, CuisineRequest request){
        Cuisine cuisine = findCuisine(id);
        boolean nameChanged = !cuisine.getName().equalsIgnoreCase(request.name());
        if (nameChanged && cuisineRepository.existsByNameIgnoreCase(request.name())){
            throw new DuplicateResourceException("Cuisine", request.name());
        }

        cuisine.setName(request.name());
        cuisine.setDescription(request.description());

        return cuisineMapper.toResponse(cuisine);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.CUISINES, key = "#id"),
            @CacheEvict(cacheNames = CacheNames.CUISINES, key = "'all'")
    })
    @Transactional
    public void deleteCuisine(Long id){
        if (restaurantRepository.existsByCuisineId(id)){
            throw new ResourceInUseException("Cuisine", id, "Restaurant");
        }
        cuisineRepository.delete(findCuisine(id));
    }

    private Cuisine findCuisine(Long id){
        return cuisineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuisine", id));
    }
}
