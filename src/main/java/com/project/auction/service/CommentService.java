package com.project.auction.service;

import com.project.auction.model.Comment;
import com.project.auction.model.Listing;
import com.project.auction.model.User;

import java.util.List;

public interface CommentService {
    List<Comment> getAllByListing(Listing l);
    Comment save(User user, Long listingId, String content);
}
