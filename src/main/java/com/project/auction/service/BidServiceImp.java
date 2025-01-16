package com.project.auction.service;

import com.project.auction.model.Bid;
import com.project.auction.model.Listing;
import com.project.auction.repository.BidRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidServiceImp implements BidService {

    private final BidRepository bidRepository;

    public BidServiceImp(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    @Override
    public List<Bid> getAllBidByListing(Listing l) {
        return bidRepository.getAllByListing(l);
    }
}
