package com.backend.foodmenu.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record OfferRequest(

        @NotBlank(message = "Title is required!")
        @Size(max = 100, message = "Title must be at most 100 characters!")
        String title,

        @Size(max = 255, message = "Description must be at most 255 characters!")
        String description,

        @NotNull(message = "Discount percent is required!")
        @Min(value = 1, message = "Discount must be at least 1%")
        @Max(value = 90, message = "Discount must be at most 90%")
        Integer discountPercent,

        @NotNull(message = "Valid from is required!")
        LocalDateTime validFrom,

        @NotNull(message = "Valid till is required!")
        LocalDateTime validTill
) {
}
