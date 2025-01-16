package com.project.auction.repository;

import com.project.auction.model.Listing;
import com.project.auction.model.User;
import com.project.auction.model.WatchList;
import com.project.auction.model.WatchListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchList, WatchListId> {
    List<WatchList> getAllByUser(User user);

    boolean existsByUserAndListing(User user, Listing listing);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO watch_list(user_id, listing_id) VALUES (:userId, :listingId)", nativeQuery = true)
    void saveByEntityIds(@Param("userId") Long userId, @Param("listingId") Long listingId);
}
