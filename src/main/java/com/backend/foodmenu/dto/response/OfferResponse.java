package com.backend.foodmenu.dto.response;

import com.backend.foodmenu.entity.Restaurant;

import java.time.LocalDateTime;

public record OfferResponse(
        Long id,
        String title,
        String description,
        int discountPercent,
        LocalDateTime validFrom,
        LocalDateTime validTill,
        Long restaurantId
) {
}
