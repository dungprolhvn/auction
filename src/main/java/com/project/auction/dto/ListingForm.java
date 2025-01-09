package com.project.auction.dto;

import com.project.auction.model.Category;
import com.project.auction.model.Listing;
import com.project.auction.model.User;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

@Data
public class ListingForm {
    @Length(min = 5, max = 200)
    private String name;

    @Length(min = 20, max = 65535)
    private String description;

    @Length(max = 256)
    @URL
    private String image;

    @Positive
    private BigDecimal startingBid;

    private Category category;

    public Listing toListing(User user) {
        return new Listing(user, category, name, description, image, startingBid);
    }

}
