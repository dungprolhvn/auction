package com.project.auction.service;

import com.project.auction.model.Listing;
import com.project.auction.repository.ListingRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class ListingServiceImp implements ListingService {

    private final ListingRepository listingRepository;

    public ListingServiceImp(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    @Override
    public boolean save(Listing l) {
        try {
            listingRepository.save(l);
            return true;
        }
        catch (DataAccessException e) {
            return false;
        }
    }
}
