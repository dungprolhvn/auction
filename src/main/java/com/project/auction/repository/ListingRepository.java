package com.project.auction.repository;

import com.project.auction.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {
    List<Listing> getAllByIsClosedFalse();

    List<Listing> getAllByIsClosedFalseAndCategoryId(long id);

    List<Listing> searchByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String searchQuery, String searchQuery1);
}
