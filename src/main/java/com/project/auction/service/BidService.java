package com.project.auction.service;

import com.project.auction.model.Bid;
import com.project.auction.model.Listing;
import com.project.auction.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface BidService {
    List<Bid> getAllBidByListing(Listing l);

    BigDecimal getMinBid(Long listingId);

    String saveNewBid(User currentUser, Listing listing, BigDecimal bidValue);
}
