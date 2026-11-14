package com.backend.foodmenu.dto.request;

import jakarta.validation.constraints.*;

import java.util.Set;

public record RestaurantRequest(

        @NotBlank(message = "Name is required!")
        @Size(max = 150, message = "Name must be at most 150 characters!")
        String name,

        @NotBlank(message = "Address is required!")
        @Size(max = 255, message = "Address must be at most 255 characters!")
        String address,

        @NotNull(message = "Rating is required!")
        @DecimalMin(value = "0.0", message = "Rating must be at least 0")
        @DecimalMax(value = "5.0", message = "Rating must be at most 5")
        Double rating,

        @NotNull(message = "City is required!")
        Long cityId,

        @NotEmpty(message = "At least one cuisine is required!")
        Set<Long> cuisineIds
) {
}
