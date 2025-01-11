package com.project.auction.controller;

import com.project.auction.service.ListingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @GetMapping("/active-listing")
    public String activeListing(Model model) {
        model.addAttribute(
                "activeListings",
                listingService.getActiveListings());
        return "activelistings";
    }
}
