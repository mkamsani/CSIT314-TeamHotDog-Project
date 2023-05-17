package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "rating_review")
public class RatingReview {
    @Id
    @Column(name = "uuid", nullable = false)
    protected UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uuid", nullable = false)
    protected LoyaltyPoint loyaltyPoint;

    @Column(name = "rating", nullable = false)
    protected Integer rating;

    @Column(name = "review", nullable = false, length = Integer.MAX_VALUE)
    protected String review;

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
}