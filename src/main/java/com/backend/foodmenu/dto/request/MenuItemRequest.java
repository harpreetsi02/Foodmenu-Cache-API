package com.backend.foodmenu.dto.request;

import com.backend.foodmenu.entity.MenuCategory;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record MenuItemRequest(

        @NotBlank(message = "Name is required!")
        @Size(max = 150, message = "Name must be at most 150 characters!")
        String name,

        @Size(max = 255, message = "Description must be at most 255 characters!")
        String description,

        @NotNull(message = "Price is required!")
        @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
        @Digits(integer = 8, fraction = 2, message = "Price can have at most 8 digits and 2 decimals!")
        BigDecimal price,

        @NotNull(message = "Category is required!")
        MenuCategory category,

        @NotNull(message = "Vegetarian flag is required!")
        Boolean vegetarian,

        Boolean available
) {
}
