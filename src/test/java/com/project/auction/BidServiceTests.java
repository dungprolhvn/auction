package com.project.auction;

import com.project.auction.model.Bid;
import com.project.auction.model.Listing;
import com.project.auction.model.User;
import com.project.auction.service.BidService;
import com.project.auction.repository.BidRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class BidServiceTest {

    @Autowired
    private BidService bidService;

    @MockBean
    private BidRepository bidRepository;

    private Listing listing;
    private List<User> users;
    private static final int NUMBER_OF_BIDDERS = 100;
    private static final BigDecimal BID_AMOUNT = new BigDecimal("100.00");
    private volatile BigDecimal currentMaxBid;  // Track current highest bid

    @BeforeEach
    void setUp() {
        // Create a test listing
        User auctioneer = new User();
        auctioneer.setId(1L);
        auctioneer.setUsername("auctioneer");

        listing = new Listing();
        listing.setId(1L);
        listing.setName("Test Item");
        listing.setAuctioneer(auctioneer);
        listing.setStartingBid(new BigDecimal("50.00"));
        listing.setClosed(false);

        // Initialize current max bid
        currentMaxBid = listing.getStartingBid();

        // Create test bidders
        users = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_BIDDERS; i++) {
            User bidder = new User();
            bidder.setId(Long.valueOf(i + 2));  // IDs start after auctioneer's ID
            bidder.setUsername("bidder" + (i + 1));
            users.add(bidder);
        }

        // Setup mock behavior with atomic updates
        when(bidRepository.getMinBidPriceByListingId(listing.getId()))
                .thenAnswer(invocation -> currentMaxBid);

        when(bidRepository.save(any(Bid.class))).thenAnswer(invocation -> {
            Bid savedBid = invocation.getArgument(0);
            // Update current max bid if this bid is higher
            synchronized(this) {
                if (savedBid.getBidPrice().compareTo(currentMaxBid) > 0) {
                    currentMaxBid = savedBid.getBidPrice();
                }
            }
            return savedBid;
        });
    }

    @Test
    void testConcurrentBidding() throws InterruptedException {
        int numberOfThreads = NUMBER_OF_BIDDERS;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch readyLatch = new CountDownLatch(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        AtomicInteger successfulBids = new AtomicInteger(0);
        List<String> results = new ArrayList<>();

        // Submit concurrent bid tasks
        for (User user : users) {
            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    // Wait for all threads to be ready
                    startLatch.await();

                    // Place bid
                    String result = bidService.saveNewBid(user, listing, BID_AMOUNT);
                    synchronized (results) {
                        results.add(result);
                    }
                    if ("Placed bid".equals(result)) {
                        successfulBids.incrementAndGet();
                    }
                } catch (Exception e) {
                    fail("Thread interrupted: " + e.getMessage());
                }
            });
        }

        // Wait for all threads to be ready
        readyLatch.await();
        // Start all threads simultaneously
        startLatch.countDown();

        // Shutdown executor and wait for completion
        executorService.shutdown();
        boolean completed = executorService.awaitTermination(10, TimeUnit.SECONDS);
        assertTrue(completed, "Test didn't complete within timeout");

        // Print results for debugging
        System.out.println("Final max bid: " + currentMaxBid);
        System.out.println("Successful bids: " + successfulBids.get());
        results.forEach(System.out::println);

        // Verify results
        assertEquals(NUMBER_OF_BIDDERS, results.size(), "Not all bids were processed");
        assertEquals(1, successfulBids.get(), "Expected only one successful bid");

        // Most bids should fail with "Your bid must be higher than max bid"
        long failedBids = results.stream()
                .filter(r -> r.equals("Your bid must be higher than max bid"))
                .count();
        assertTrue(failedBids > 0, "Expected some bids to fail due to bid amount");

        // Verify bid was saved exactly once
        verify(bidRepository, times(1)).save(any(Bid.class));
    }
}