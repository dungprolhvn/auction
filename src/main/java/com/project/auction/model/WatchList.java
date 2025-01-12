package com.project.auction.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "watch_list")
@IdClass(WatchListId.class)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class WatchList {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WatchList watchList = (WatchList) o;
        return Objects.equals(user, watchList.user) && Objects.equals(listing, watchList.listing);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(user, listing);
    }
}