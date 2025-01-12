package com.project.auction.service;

import com.project.auction.model.Listing;
import com.project.auction.model.User;

import java.util.List;

public interface WatchlistService {
    List<Listing> getWatchlist(User user);
}
