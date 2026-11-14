package com.backend.foodmenu.controller;

import com.backend.foodmenu.dto.request.CityRequest;
import com.backend.foodmenu.dto.response.CityResponse;
import com.backend.foodmenu.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@Tag(name = "Cities", description = "City management APIs")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService){
        this.cityService = cityService;
    }

    @Operation(summary = "Create a city")
    @PostMapping
    public ResponseEntity<CityResponse> createCity(
            @Valid @RequestBody CityRequest request
    ) {
        CityResponse response =
                cityService.createCity(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get a city by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getCityById(
            @PathVariable Long id
    ) {
        CityResponse response =
                cityService.getCityById(id);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List all cities")
    @GetMapping
    public ResponseEntity<List<CityResponse>> getAllCities(){
        List<CityResponse> responses =
                cityService.findAllCities();

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Update a city")
    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> updateCity(
            @PathVariable Long id,
            @Valid @RequestBody CityRequest request
    ) {
        CityResponse response =
                cityService.updateCity(id, request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a city")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(
            @PathVariable Long id
    ) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }
}
