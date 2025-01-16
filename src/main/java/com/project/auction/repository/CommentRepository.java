package com.project.auction.repository;

import com.project.auction.model.Comment;
import com.project.auction.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getAllByListing(Listing l);
}
