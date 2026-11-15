package com.backend.foodmenu.controller;

import com.backend.foodmenu.dto.response.OfferResponse;
import com.backend.foodmenu.service.OfferService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@Tag(name = "City Offers", description = "City offers APIs management.")
public class CityOfferController {

    private final OfferService offerService;

    public CityOfferController(OfferService offerService){
        this.offerService = offerService;
    }

    @GetMapping("/active")
    public ResponseEntity<List<OfferResponse>> getAllOffersByCity(
            @RequestParam Long cityId
    ) {
        List<OfferResponse> responses =
                offerService.getActiveOffersByCity(cityId);

        return ResponseEntity.ok(responses);
    }
}
