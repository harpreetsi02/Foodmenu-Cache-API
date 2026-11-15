package com.backend.foodmenu.controller;

import com.backend.foodmenu.dto.request.OfferRequest;
import com.backend.foodmenu.dto.response.OfferResponse;
import com.backend.foodmenu.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/offers")
@Tag(name = "Offers", description = "Offer APIs of a restaurant")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @Operation(summary = "Create an offer for a restaurant")
    @PostMapping
    public ResponseEntity<OfferResponse> create(@PathVariable Long restaurantId,
                                                @Valid @RequestBody OfferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(offerService.createOffer(restaurantId, request));
    }

    @Operation(summary = "List all offers (past, current, future) of a restaurant")
    @GetMapping
    public ResponseEntity<List<OfferResponse>> getAll(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(offerService.getAllOffers(restaurantId));
    }

    @Operation(summary = "List currently valid offers of a restaurant")
    @GetMapping("/active")
    public ResponseEntity<List<OfferResponse>> getActive(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(offerService.getActiveOffers(restaurantId));
    }

    @Operation(summary = "Update an offer")
    @PutMapping("/{id}")
    public ResponseEntity<OfferResponse> update(@PathVariable Long restaurantId, @PathVariable Long id,
                                                @Valid @RequestBody OfferRequest request) {
        return ResponseEntity.ok(offerService.updateOffer(restaurantId, id, request));
    }

    @Operation(summary = "Delete an offer")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long restaurantId, @PathVariable Long id) {
        offerService.deleteOffer(restaurantId, id);
        return ResponseEntity.noContent().build();
    }
}