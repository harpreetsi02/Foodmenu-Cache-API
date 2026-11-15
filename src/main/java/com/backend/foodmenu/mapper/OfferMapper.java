package com.backend.foodmenu.mapper;

import com.backend.foodmenu.dto.request.OfferRequest;
import com.backend.foodmenu.dto.response.OfferResponse;
import com.backend.foodmenu.entity.Offer;
import com.backend.foodmenu.entity.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class OfferMapper {

    public OfferResponse toResponse(Offer offer){

        return new OfferResponse(
                offer.getId(),
                offer.getTitle(),
                offer.getDescription(),
                offer.getDiscountPercent(),
                offer.getValidFrom(),
                offer.getValidTill(),
                offer.getRestaurant().getId()
        );
    }

    public Offer toEntity(OfferRequest request, Restaurant restaurant){

        return new Offer(
                request.title(),
                request.description(),
                request.discountPercent(),
                request.validFrom(),
                request.validTill(),
                restaurant
        );
    }
}
