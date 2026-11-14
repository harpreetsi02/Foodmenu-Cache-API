package com.backend.foodmenu.controller;

import com.backend.foodmenu.dto.request.CuisineRequest;
import com.backend.foodmenu.dto.response.CuisineResponse;
import com.backend.foodmenu.service.CuisineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuisines")
@Tag(name = "Cuisines", description = "Cuisine management APIs")
public class CuisineController {

    private final CuisineService cuisineService;

    public CuisineController(CuisineService cuisineService){
        this.cuisineService = cuisineService;
    }

    @Operation(summary = "Create a cuisine")
    @PostMapping
    public ResponseEntity<CuisineResponse> createCuisine(
            @Valid @RequestBody CuisineRequest request
    ) {
        CuisineResponse response =
                cuisineService.createCuisine(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get a cuisine by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CuisineResponse> getCuisineById(
            @PathVariable Long id
    ) {
        CuisineResponse response =
                cuisineService.getCuisineById(id);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List all cuisines")
    @GetMapping
    public ResponseEntity<List<CuisineResponse>> getAllCuisine(){
        List<CuisineResponse> responses =
                cuisineService.getAllCuisines();

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Update a cuisine")
    @PutMapping("/{id}")
    public ResponseEntity<CuisineResponse> updateCuisine(
            @PathVariable Long id,
            @Valid @RequestBody CuisineRequest request
    ) {
        CuisineResponse response =
                cuisineService.updateCuisine(id, request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a cuisine")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuisine(
            @PathVariable Long id
    ) {
        cuisineService.deleteCuisine(id);
        return ResponseEntity.noContent().build();
    }
}
