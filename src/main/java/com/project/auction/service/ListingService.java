package com.project.auction.service;

import com.project.auction.model.Listing;
import com.project.auction.model.User;

import java.util.List;

public interface ListingService {
    boolean save(Listing l);

    List<Listing> getActiveListings();

    List<Listing> getActiveListingsByCategory(String categoryId);

    Listing getById(Long id);

    String closeListing(User currentUser, Listing l);

    List<Listing> getAllListings();
}
