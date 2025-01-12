package com.project.auction.service;

import com.project.auction.model.Listing;
import com.project.auction.model.User;
import com.project.auction.model.WatchList;
import com.project.auction.repository.WatchlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchlistServiceImp implements WatchlistService {

    private final WatchlistRepository watchlistRepository;

    public WatchlistServiceImp(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    @Override
    public List<Listing> getWatchlist(User user) {
        List<WatchList> watchlist = watchlistRepository.getAllByUser(user);
        return watchlist.stream()
                .map(WatchList::getListing)
                .collect(Collectors.toList());
    }
}
