package com.project.auction.controller;

import com.project.auction.model.Bid;
import com.project.auction.model.Comment;
import com.project.auction.model.Listing;
import com.project.auction.model.User;
import com.project.auction.service.BidService;
import com.project.auction.service.CommentService;
import com.project.auction.service.ListingService;
import com.project.auction.service.WatchlistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ListingController {

    private final ListingService listingService;
    private final WatchlistService watchlistService;
    private final BidService bidService;
    private final CommentService commentService;

    public ListingController(ListingService listingService, WatchlistService watchlistService, BidService bidService, CommentService commentService) {
        this.listingService = listingService;
        this.watchlistService = watchlistService;
        this.bidService = bidService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String activeListing(Model model) {
        model.addAttribute(
                "activeListings",
                listingService.getActiveListings());
        return "home";
    }

    @GetMapping(value = "/listing/{id}")
    public String listingDetail(
            Authentication authentication,
            @PathVariable("id") Long listingId,
            Model model) {
        Listing l = listingService.getById(listingId);
        // add to/remove from watchlist button
        User currentUser = (User) authentication.getPrincipal();
        boolean inWatchlist = watchlistService.checkListingInUserWatchlist(currentUser, l);
        model.addAttribute("inWatchlist", inWatchlist);
        // get list of bid placed to display
        List<Bid> bids = bidService.getAllBidByListing(l);
        model.addAttribute("allBid", bids);
        // check if current user can bid
        boolean biddable = !currentUser.equals(l.getAuctioneer()) && !l.isClosed();
        if (biddable) {
            // find the minimum bid price
            BigDecimal minBid = bids.stream()
                    .map(Bid::getBidPrice)
                    .max(Comparator.naturalOrder())
                    .orElse(l.getStartingBid());
            model.addAttribute("minBid", minBid);
            // TODO: handle bid place logic (race condition)
        }
        // get list of comments to display (handle using REST API)
        List<Comment> comments = commentService.getAllByListing(l);
        model.addAttribute("allComment", comments);
        // add the listing to model
        model.addAttribute("listing", l);
        return "listing";
    }

    @PostMapping(value = "/listing/comment", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> processComment(
            Authentication authentication,
            @RequestBody Map<String, String> payload) {
        User user = (User) authentication.getPrincipal();
        Long listingId = Long.parseLong(payload.get("listingId"));
        String content = payload.get("content");
        try {
            Comment c = commentService.save(user, listingId, content);
            Map<String, String> response = Map.of(
                    "username", c.getUser().getUsername(),
                    "publishedAt", String.valueOf(c.getPublishedAt()),
                    "content", c.getContent());
            return ResponseEntity.ok(response);
        }
        catch (DataAccessException de) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }
    }

    @PostMapping("/listing/{id}/bid")
    public String handleBidPlace(
            Authentication authentication,
            @PathVariable("id") Long listingId,
            @RequestParam("bidPrice") BigDecimal bidValue,
            RedirectAttributes redirectAttributes) {
        User currentUser = (User) authentication.getPrincipal();
        Listing l = listingService.getById(listingId);
        String result = bidService.saveNewBid(currentUser, l, bidValue);
        redirectAttributes.addFlashAttribute("successMessage", result);
        return "redirect:/listing/" + listingId;
    }

}
