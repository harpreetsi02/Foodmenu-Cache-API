package com.backend.foodmenu.controller;

import com.backend.foodmenu.dto.request.MenuItemRequest;
import com.backend.foodmenu.dto.response.MenuItemResponse;
import com.backend.foodmenu.entity.MenuCategory;
import com.backend.foodmenu.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu-items")
@Tag(name = "Menu Items", description = "Menu Item APIs of a restaurant")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService){
        this.menuItemService = menuItemService;
    }

    @Operation(summary = "Add a menu item to a restaurant")
    @PostMapping
    public ResponseEntity<MenuItemResponse> createMenuItem(
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuItemRequest request
    ) {
        MenuItemResponse response =
                menuItemService.createMenuItem(restaurantId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Search menu items (optional filters, paginated)")
    @GetMapping
    public ResponseEntity<Page<MenuItemResponse>> searchMenuItem(
            @PathVariable Long restaurantId,
            @RequestParam(required = false)MenuCategory category,
            @RequestParam(required = false)Boolean vegetarian,
            @RequestParam(required = false)BigDecimal maxPrice,
            @PageableDefault(size = 10, sort = "id")Pageable pageable
    ) {
        Page<MenuItemResponse> responses =
                menuItemService.searchMenuItems(restaurantId, category, vegetarian, maxPrice, pageable);

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get a menu item")
    @GetMapping("/{menuItemId}")
    public ResponseEntity<MenuItemResponse> getMenuItemById(
            @PathVariable Long restaurantId,
            @PathVariable Long menuItemId
    ){
        MenuItemResponse response =
                menuItemService.getMenuItemById(restaurantId, menuItemId);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a menu item")
    @PutMapping("/{menuItemId}")
    public ResponseEntity<MenuItemResponse> update(
            @PathVariable Long restaurantId,
            @PathVariable Long menuItemId,
            @Valid @RequestBody MenuItemRequest request
    ) {
        MenuItemResponse response =
                menuItemService.updateMenuItem(restaurantId, menuItemId, request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a menu item")
    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long restaurantId,
            @PathVariable Long menuItemId
    ) {
        menuItemService.deleteMenuItem(restaurantId, menuItemId);
        return ResponseEntity.noContent().build();
    }
}
