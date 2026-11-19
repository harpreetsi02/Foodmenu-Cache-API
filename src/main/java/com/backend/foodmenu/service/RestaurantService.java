package com.backend.foodmenu.service;

import com.backend.foodmenu.config.CacheNames;
import com.backend.foodmenu.dto.request.RestaurantRequest;
import com.backend.foodmenu.dto.response.RestaurantResponse;
import com.backend.foodmenu.entity.City;
import com.backend.foodmenu.entity.Cuisine;
import com.backend.foodmenu.entity.Restaurant;
import com.backend.foodmenu.exception.DuplicateResourceException;
import com.backend.foodmenu.exception.ResourceNotFoundException;
import com.backend.foodmenu.mapper.RestaurantMapper;
import com.backend.foodmenu.repository.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CuisineRepository cuisineRepository;
    private final CityRepository cityRepository;
    private final RestaurantMapper restaurantMapper;
    private final MenuItemRepository menuItemRepository;
    private final OfferRepository offerRepository;

    public RestaurantService(
            RestaurantRepository restaurantRepository,
            CuisineRepository cuisineRepository,
            CityRepository cityRepository,
            RestaurantMapper restaurantMapper,
            MenuItemRepository menuItemRepository,
            OfferRepository offerRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.cuisineRepository = cuisineRepository;
        this.cityRepository = cityRepository;
        this.restaurantMapper = restaurantMapper;
        this.menuItemRepository = menuItemRepository;
        this.offerRepository = offerRepository;
    }

    @CacheEvict(cacheNames = CacheNames.RESTAURANTS, key = "'all'")
    @Transactional
    public RestaurantResponse createRestaurant(RestaurantRequest request){
        City city = findCity(request.cityId());
        Set<Cuisine> cuisines = resolveCuisines(request.cuisineIds());

        if (restaurantRepository.existsByNameIgnoreCaseAndCity_Id(request.name(), city.getId())){
            throw new DuplicateResourceException("Restaurant", request.name());
        }

        Restaurant restaurant = restaurantMapper.toEntity(request, city, cuisines);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(savedRestaurant);
    }

    @Cacheable(cacheNames = CacheNames.RESTAURANTS, key = "#restaurantId", sync = true)
    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurantById(Long restaurantId){
        return restaurantMapper.toResponse(findRestaurant(restaurantId));
    }

    @Cacheable(cacheNames = CacheNames.RESTAURANTS, key = "'all'")
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAllRestaurants(){
        return restaurantRepository.findAll()
                .stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    @Caching(
            put = @CachePut(cacheNames = CacheNames.RESTAURANTS, key = "#restaurantId"),
            evict = @CacheEvict(cacheNames = CacheNames.RESTAURANTS, key = "'all'")
    )
    @Transactional
    public RestaurantResponse updateRestaurant(Long restaurantId, RestaurantRequest request){
        Restaurant restaurant = findRestaurant(restaurantId);
        City city = findCity(request.cityId());
        Set<Cuisine> cuisines = resolveCuisines(request.cuisineIds());

        boolean sameNameAndCity = restaurant.getName().equalsIgnoreCase(request.name())
                && restaurant.getCity().getId().equals(city.getId());

        if (!sameNameAndCity && restaurantRepository.existsByNameIgnoreCaseAndCity_Id(request.name(), city.getId())){
            throw new DuplicateResourceException("Restaurant", request.name());
        }

        restaurant.setName(request.name());
        restaurant.setAddress(request.address());
        restaurant.setRating(request.rating());
        restaurant.setCity(city);
        restaurant.setCuisines(cuisines);

        return restaurantMapper.toResponse(restaurant);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.RESTAURANTS, key = "#restaurantId"),
            @CacheEvict(cacheNames = CacheNames.RESTAURANTS, key = "'all'"),
            @CacheEvict(cacheNames = CacheNames.MENU_ITEMS, allEntries = true),
            @CacheEvict(cacheNames = CacheNames.ACTIVE_OFFERS, allEntries = true)
    })
    @Transactional
    public void deleteRestaurant(Long restaurantId){
        Restaurant restaurant = findRestaurant(restaurantId);
        menuItemRepository.deleteAllByRestaurantId(restaurantId);
        offerRepository.deleteAllByRestaurantId(restaurantId);
        restaurantRepository.delete(restaurant);
    }

    private Restaurant findRestaurant(Long restaurantId){
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));
    }

    private City findCity(Long cityId){
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City", cityId));
    }

    private Set<Cuisine> resolveCuisines(Set<Long> ids){
        List<Cuisine> found = cuisineRepository.findAllById(ids);
        if (found.size() != ids.size()){
            Set<Long> foundIds = found.stream()
                    .map(Cuisine::getId)
                    .collect(Collectors.toSet());

            Long missingId = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .findFirst()
                    .orElseThrow();

            throw new ResourceNotFoundException("Cuisine", missingId);
        }

        return new HashSet<>(found);
    }
}
