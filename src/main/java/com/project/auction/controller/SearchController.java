package com.project.auction.controller;

import com.project.auction.model.Listing;
import com.project.auction.repository.ListingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final ListingRepository listingRepository;

    public SearchController(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    @GetMapping(value = "/search", params = "q")
    public String processSearch(
            @RequestParam("q") String searchQuery,
            Model model) {
        List<Listing> result = listingRepository.searchByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchQuery, searchQuery);
        model.addAttribute("searchResult", true);
        model.addAttribute("activeListings", result);
        return "home";
    }
}
