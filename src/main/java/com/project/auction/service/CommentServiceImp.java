package com.project.auction.service;

import com.project.auction.model.Comment;
import com.project.auction.model.Listing;
import com.project.auction.model.User;
import com.project.auction.repository.CommentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImp implements CommentService {

    private final CommentRepository commentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public CommentServiceImp(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getAllByListing(Listing l) {
        return commentRepository.getAllByListing(l);
    }

    @Override
    public Comment save(User user, Long listingId, String content) {
        Listing listing = entityManager.getReference(Listing.class, listingId); // Use proxy object
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setListing(listing);
        comment.setContent(content);
        return commentRepository.save(comment);
    }
}
