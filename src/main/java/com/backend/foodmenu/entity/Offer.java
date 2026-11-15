package com.backend.foodmenu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "offers")
@Getter
@Setter
@NoArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private int discountPercent;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    private LocalDateTime validTill;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Offer(
            String title, String description, int discountPercent,
            LocalDateTime validFrom, LocalDateTime validTill, Restaurant restaurant
    ) {
        this.title = title;
        this.description = description;
        this.discountPercent = discountPercent;
        this.validFrom = validFrom;
        this.validTill = validTill;
        this.restaurant = restaurant;
    }
}
