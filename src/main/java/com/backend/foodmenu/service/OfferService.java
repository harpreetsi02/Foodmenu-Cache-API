package com.backend.foodmenu.service;

import com.backend.foodmenu.config.CacheNames;
import com.backend.foodmenu.dto.request.OfferRequest;
import com.backend.foodmenu.dto.response.OfferResponse;
import com.backend.foodmenu.entity.Offer;
import com.backend.foodmenu.entity.Restaurant;
import com.backend.foodmenu.exception.InvalidRequestException;
import com.backend.foodmenu.exception.ResourceNotFoundException;
import com.backend.foodmenu.mapper.OfferMapper;
import com.backend.foodmenu.repository.CityRepository;
import com.backend.foodmenu.repository.OfferRepository;
import com.backend.foodmenu.repository.RestaurantRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OfferMapper offerMapper;
    private final CityRepository cityRepository;

    public OfferService(
            OfferRepository offerRepository,
            RestaurantRepository restaurantRepository,
            OfferMapper offerMapper,
            CityRepository cityRepository
    ) {
        this.offerRepository = offerRepository;
        this.restaurantRepository = restaurantRepository;
        this.offerMapper = offerMapper;
        this.cityRepository = cityRepository;
    }

    @CacheEvict(cacheNames = CacheNames.ACTIVE_OFFERS, allEntries = true)
    @Transactional
    public OfferResponse createOffer(Long restaurantId, OfferRequest request){
        Restaurant restaurant = findRestaurant(restaurantId);
        validateDates(request);

        Offer offer = offerMapper.toEntity(request, restaurant);
        Offer savedOffer = offerRepository.save(offer);
        return offerMapper.toResponse(savedOffer);
    }

    @Cacheable(cacheNames = CacheNames.ACTIVE_OFFERS, key = "'restaurant:' + #restaurantId")
    @Transactional(readOnly = true)
    public List<OfferResponse> getAllOffers(Long restaurantId){

        return offerRepository.findByRestaurant_IdOrderByValidFromDesc(restaurantId)
                .stream()
                .map(offerMapper::toResponse)
                .toList();
    }

    @Cacheable(cacheNames = CacheNames.ACTIVE_OFFERS, key = "'restaurant:' + #restaurantId")
    @Transactional(readOnly = true)
    public List<OfferResponse> getActiveOffers(Long restaurantId){
        findRestaurant(restaurantId);
        return offerRepository.findActiveByRestaurant(restaurantId, LocalDateTime.now())
                .stream()
                .map(offerMapper::toResponse)
                .toList();
    }

    @Cacheable(cacheNames = CacheNames.ACTIVE_OFFERS, key = "'city:' + #cityId")
    @Transactional(readOnly = true)
    public List<OfferResponse> getActiveOffersByCity(Long cityId){
        cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City", cityId));

        return offerRepository.findActiveByCity(cityId, LocalDateTime.now())
                .stream()
                .map(offerMapper::toResponse)
                .toList();
    }

    @CacheEvict(cacheNames = CacheNames.ACTIVE_OFFERS, allEntries = true)
    @Transactional
    public OfferResponse updateOffer(Long restaurantId, Long offerId, OfferRequest request) {
        Offer offer = findOffer(restaurantId, offerId);
        validateDates(request);

        offer.setTitle(request.title());
        offer.setDescription(request.description());
        offer.setDiscountPercent(request.discountPercent());
        offer.setValidFrom(request.validFrom());
        offer.setValidTill(request.validTill());
        return offerMapper.toResponse(offer);
    }

    @CacheEvict(cacheNames = CacheNames.ACTIVE_OFFERS, allEntries = true)
    @Transactional
    public void deleteOffer(Long restaurantId, Long offerId) {
        offerRepository.delete(findOffer(restaurantId, offerId));
    }

    private void validateDates(OfferRequest request){
        if (!request.validTill().isAfter(request.validFrom())){
            throw new InvalidRequestException("Validate must be after validFrom");
        }
    }

    private Restaurant findRestaurant(Long restaurantId){
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));
    }

    private Offer findOffer(Long restaurantId, Long offerId){
        return offerRepository.findByIdAndRestaurant_Id(offerId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Offer", offerId));
    }
}
