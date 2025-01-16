package com.project.auction.controller;

import com.project.auction.model.Listing;
import com.project.auction.model.User;
import com.project.auction.service.WatchlistService;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping(value = "/api/watchlist/add", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> addToWatchList(
            Authentication authentication,
            @RequestBody Map<String, Long> payload) {
        User currentUser = (User) authentication.getPrincipal();
        Long listingId = payload.get("id");
        try {
            watchlistService.addToWatchlist(currentUser, listingId);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Added to watchlist\"}");
        }
        catch (DataAccessException de) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Bad request\"}");
        }
    }

    @PostMapping(value = "/api/watchlist/remove", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> removeFromWatchlist(
            Authentication authentication,
            @RequestBody Map<String, Long> payload) {
        User currentUser = (User) authentication.getPrincipal();
        Long listingId = payload.get("id");
        try {
            watchlistService.removeFromWatchlist(currentUser, listingId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        }
        catch (DataAccessException de) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Bad request\"}");
        }
    }

}
