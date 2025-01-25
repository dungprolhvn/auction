package com.project.auction.service;

import com.project.auction.model.Bid;
import com.project.auction.model.Listing;
import com.project.auction.model.User;
import com.project.auction.repository.BidRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class BidServiceImp implements BidService {

    private final BidRepository bidRepository;

    @Getter
    private final Map<Long, ReentrantLock> listingLocks = new ConcurrentHashMap<>();

    public BidServiceImp(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    @Override
    public List<Bid> getAllBidByListing(Listing l) {
        return bidRepository.getAllByListingOrderByPlacedAtDesc(l);
    }

    @Override
    public BigDecimal getMinBid(Long listingId) {
        return bidRepository.getMinBidPriceByListingId(listingId);
    }

    @Override
    public String saveNewBid(User user, Listing listing, BigDecimal bidValue) {
        if (bidValue == null) {
            return "Bid value cannot be null";
        }
        ReentrantLock listingLock = listingLocks.computeIfAbsent(
                listing.getId(),
                k -> new ReentrantLock()
        );
        listingLock.lock();
        try {
            if (listing.isClosed()) {
                return "Listing closed";
            }
            if (user.equals(listing.getAuctioneer())) {
                return "Cant place bid on you own listing";
            }
            BigDecimal minBid = getMinBid(listing.getId());
            if (minBid == null) {
                minBid = listing.getStartingBid();
            }
            if (bidValue.compareTo(minBid) <= 0) {
                return "Your bid must be higher than max bid";
            }
            try {
                Bid b = new Bid(listing, user, bidValue);
                bidRepository.save(b);
                log.info("User {} placed bid {} on listing {}", user.getUsername(), bidValue.toString(), listing.getName());
                return "Placed bid";
            }
            catch (DataAccessException de) {
                log.debug("Error when bidding {}", de.toString());
                return "Some error occurred";
            }
        }
        finally {
            listingLock.unlock();
        }
    }

}
