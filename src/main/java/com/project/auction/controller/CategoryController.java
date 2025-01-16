package com.project.auction.controller;

import com.project.auction.model.Listing;
import com.project.auction.service.CategoryService;
import com.project.auction.service.ListingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;


@Controller
@SessionAttributes("categories")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;
    private final ListingService listingService;

    public CategoryController(CategoryService categoryService, ListingService listingService) {
        this.categoryService = categoryService;
        this.listingService = listingService;
    }

    @GetMapping("/categories")
    public String categoryView(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "categories";
    }

    @GetMapping(value = "/categories", params = "id")
    public String listingsByCategory(@RequestParam("id") String categoryId, Model model) {
        List<Listing> listings = listingService.getActiveListingsByCategory(categoryId);
        model.addAttribute("categoryid", categoryId);
        model.addAttribute("activeListings", listings);
        return "home";
    }

}
