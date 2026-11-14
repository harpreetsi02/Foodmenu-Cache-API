package com.backend.foodmenu.controller;

import com.backend.foodmenu.dto.request.RestaurantRequest;
import com.backend.foodmenu.dto.response.RestaurantResponse;
import com.backend.foodmenu.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurants", description = "Restaurant management APIs")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    @Operation(summary = "Create a restaurant")
    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @Valid @RequestBody RestaurantRequest request
    ) {
        RestaurantResponse response =
                restaurantService.createRestaurant(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get a restaurant by ID")
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(
            @PathVariable Long restaurantId
    ){
        RestaurantResponse response =
                restaurantService.getRestaurantById(restaurantId);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List all restaurants")
    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(){
        List<RestaurantResponse> responses =
                restaurantService.getAllRestaurants();

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Update a restaurant")
    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(
            @PathVariable Long restaurantId,
            @Valid @RequestBody RestaurantRequest request
    ) {
        RestaurantResponse response =
                restaurantService.updateRestaurant(restaurantId, request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a restaurant")
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(
            @PathVariable Long restaurantId
    ) {
        restaurantService.deleteRestaurant(restaurantId);

        return ResponseEntity.noContent().build();
    }
}
