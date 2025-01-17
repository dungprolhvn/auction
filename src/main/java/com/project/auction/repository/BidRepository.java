package com.project.auction.repository;

import com.project.auction.model.Bid;
import com.project.auction.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> getAllByListingOrderByPlacedAtDesc(Listing l);

    @Query("SELECT MAX(b.bidPrice) FROM Bid b WHERE b.listing.id = :listingId")
    BigDecimal getMinBidPriceByListingId(@Param("listingId") Long listingId);
}
