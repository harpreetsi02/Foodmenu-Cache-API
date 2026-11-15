package com.backend.foodmenu.service;

import com.backend.foodmenu.dto.request.MenuItemRequest;
import com.backend.foodmenu.dto.response.MenuItemResponse;
import com.backend.foodmenu.entity.MenuCategory;
import com.backend.foodmenu.entity.MenuItem;
import com.backend.foodmenu.entity.Restaurant;
import com.backend.foodmenu.exception.DuplicateResourceException;
import com.backend.foodmenu.exception.ResourceNotFoundException;
import com.backend.foodmenu.mapper.MenuItemMapper;
import com.backend.foodmenu.repository.MenuItemRepository;
import com.backend.foodmenu.repository.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemMapper menuItemMapper;

    public MenuItemService(
            MenuItemRepository menuItemRepository,
            RestaurantRepository restaurantRepository,
            MenuItemMapper menuItemMapper
    ) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemMapper = menuItemMapper;
    }

    @Transactional
    public MenuItemResponse createMenuItem(Long restaurantId, MenuItemRequest request){
        Restaurant restaurant = findRestaurant(restaurantId);

        if (menuItemRepository.existsByNameIgnoreCaseAndRestaurant_Id(request.name(), restaurantId)){
            throw new DuplicateResourceException("MenuItem", request.name());
        }

        MenuItem menuItem = menuItemMapper.toEntity(request, restaurant);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toResponse(savedMenuItem);
    }

    @Transactional(readOnly = true)
    public MenuItemResponse getMenuItemById(Long restaurantId, Long menuItemId){
        return menuItemMapper.toResponse(findMenuItem(menuItemId, restaurantId));
    }

    @Transactional(readOnly = true)
    public Page<MenuItemResponse> searchMenuItems(
            Long restaurantId, MenuCategory category,
            boolean vegetarian, BigDecimal maxPrice, Pageable pageable
    ) {
        findRestaurant(restaurantId);
        return menuItemRepository.search(restaurantId, category, vegetarian, maxPrice, pageable)
                .map(menuItemMapper::toResponse);
    }

    @Transactional
    public MenuItemResponse updateMenuItem(
            Long restaurantId, Long menuItemId, MenuItemRequest request
    ) {
        MenuItem menuItem = findMenuItem(restaurantId, menuItemId);

        boolean nameChanged = !menuItem.getName().equalsIgnoreCase(request.name());
        if (nameChanged && menuItemRepository.existsByNameIgnoreCaseAndRestaurant_Id(request.name(), restaurantId)){
            throw new DuplicateResourceException("MenuItem", request.name());
        }

        menuItem.setName(request.name());
        menuItem.setDescription(request.description());
        menuItem.setCategory(request.category());
        menuItem.setPrice(request.price());
        menuItem.setVegetarian(request.vegetarian());
        if (request.available() != null){
            menuItem.setAvailable(request.available());
        }

        return menuItemMapper.toResponse(menuItem);
    }

    @Transactional
    public void deleteMenuItem(Long restaurantId, Long menuItemId){
        menuItemRepository.delete(findMenuItem(menuItemId, restaurantId));
    }

    private Restaurant findRestaurant(Long restaurantId){
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));
    }

    private MenuItem findMenuItem(Long menuItemId, Long restaurantId){
        return menuItemRepository.findByIdAndRestaurant_Id(menuItemId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", menuItemId));
    }
}
