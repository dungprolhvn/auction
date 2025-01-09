package com.project.auction.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "listing")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User auctioneer;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @NonNull
    @Length(min = 5, max = 200)
    private String name;

    @NonNull
    @Length(min = 20, max = 65535)
    private String description;

    @NonNull
    @Length(max = 256)
    private String image;

    @NonNull
    private BigDecimal startingBid;

    private boolean isClosed = false;

    private LocalDateTime createdAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Listing listing = (Listing) o;
        return getId() != null && Objects.equals(getId(), listing.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
