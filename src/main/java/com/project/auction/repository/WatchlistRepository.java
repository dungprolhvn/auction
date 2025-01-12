package com.project.auction.repository;

import com.project.auction.model.User;
import com.project.auction.model.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchList, Long> {
    List<WatchList> getAllByUser(User user);
}
