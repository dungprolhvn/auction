package com.project.auction.service;

import com.project.auction.model.Bid;
import com.project.auction.model.Listing;

import java.util.List;

public interface BidService {
    List<Bid> getAllBidByListing(Listing l);
}
