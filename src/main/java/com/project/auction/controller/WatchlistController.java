package com.project.auction.controller;

import com.project.auction.model.Listing;
import com.project.auction.model.User;
import com.project.auction.service.WatchlistService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping("/watchlist")
    public String watchlist(Authentication authentication, Model model) {
        User currentUser = (User) authentication.getPrincipal();
        List<Listing> listings = watchlistService.getWatchlist(currentUser);
        model.addAttribute("allListings", listings);
        return "watchlist";
    }
}
