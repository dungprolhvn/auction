package com.project.auction.repository;

import com.project.auction.model.Bid;
import com.project.auction.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> getAllByListing(Listing l);
}
