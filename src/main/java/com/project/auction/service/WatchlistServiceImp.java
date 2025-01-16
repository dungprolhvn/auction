package com.project.auction.service;

import com.project.auction.model.Listing;
import com.project.auction.model.User;
import com.project.auction.model.WatchList;
import com.project.auction.model.WatchListId;
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

    @Override
    public boolean checkListingInUserWatchlist(User user, Listing listing) {
        return watchlistRepository.existsByUserAndListing(user, listing);
    }

    @Override
    public void addToWatchlist(User currentUser, Long listingId) {
        Long userId = currentUser.getId();
        watchlistRepository.saveByEntityIds(userId, listingId);
    }

    @Override
    public void removeFromWatchlist(User currentUser, Long listingId) {
        Long userId = currentUser.getId();
        WatchListId id = new WatchListId(userId, listingId);
        watchlistRepository.deleteById(id);
    }
}
