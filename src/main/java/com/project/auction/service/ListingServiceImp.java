package com.project.auction.service;

import com.project.auction.model.Listing;
import com.project.auction.model.User;
import com.project.auction.repository.ListingRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ListingServiceImp implements ListingService {

    private final ListingRepository listingRepository;
    private final BidService bidService;

    public ListingServiceImp(ListingRepository listingRepository, BidService bidService) {
        this.listingRepository = listingRepository;
        this.bidService = bidService;
    }

    @Override
    public boolean save(Listing l) {
        try {
            listingRepository.save(l);
            return true;
        }
        catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public List<Listing> getActiveListings() {
        return listingRepository.getAllByIsClosedFalse();
    }

    @Override
    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    @Override
    public List<Listing> getActiveListingsByCategory(String categoryId) {
        return listingRepository.getAllByIsClosedFalseAndCategoryId(Long.parseLong(categoryId));
    }

    @Override
    public Listing getById(Long id) {
        return listingRepository.getReferenceById(id);
    }

    @Override
    public String closeListing(User user, Listing l) {
        if (!user.equals(l.getAuctioneer())) {
            return "User no permisson to close";
        }
        // wait if there is saveNewBid running
        Map<Long, ReentrantLock> listingLocks = bidService.getListingLocks();
        ReentrantLock listingLock = listingLocks.computeIfAbsent(
                l.getId(),
                k -> new ReentrantLock()
        );
        try {
            if (l.isClosed()) {
                return "Listing already closed";
            }
            l.setClosed(true);
            listingRepository.save(l);
            return "Listing closed";
        }
        finally {
            listingLock.unlock();
        }
    }
}
