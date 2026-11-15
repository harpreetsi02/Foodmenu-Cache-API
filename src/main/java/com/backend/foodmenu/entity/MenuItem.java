package com.backend.foodmenu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "menu_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "restaurant_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MenuCategory category;

    @Column(nullable = false)
    private boolean vegetarian;

    @Column(nullable = false)
    private boolean available = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public MenuItem(
            String name, String description, BigDecimal price, MenuCategory category,
            boolean vegetarian, boolean available, Restaurant restaurant
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.vegetarian = vegetarian;
        this.available = available;
        this.restaurant = restaurant;
    }
}
