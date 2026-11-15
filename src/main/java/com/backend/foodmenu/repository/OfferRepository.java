package com.backend.foodmenu.repository;

import com.backend.foodmenu.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    Optional<Offer> findByIdAndRestaurant_Id(Long id, Long restaurantId);

    List<Offer> findByRestaurant_IdOrderByValidFromDesc(Long restaurantId);

    @Query("""
        SELECT o FROM Offer o
        WHERE o.restaurant.id = :restaurantId
            AND o.validFrom <= :now
            AND o.validTill >= :now
        ORDER BY o.discountPercent DESC
    """)
    List<Offer> findActiveByRestaurant(
            @Param("restaurantId") Long restaurantId,
            @Param("now")LocalDateTime now
    );

    @Query("""
        SELECT o FROM Offer o
        WHERE o.restaurant.city.id = :cityId
            AND o.validFrom <= :now
            AND o.validTill >= :now
        ORDER BY o.discountPercent DESC
    """)
    List<Offer> findActiveByCity(
            @Param("cityId") Long cityId,
            @Param("now") LocalDateTime now
    );

    @Modifying
    @Query("DELETE FROM Offer o WHERE o.restaurant.id = :restaurantId")
    void deleteAllByRestaurantId(@Param("restaurantId") Long restaurantId);
}
