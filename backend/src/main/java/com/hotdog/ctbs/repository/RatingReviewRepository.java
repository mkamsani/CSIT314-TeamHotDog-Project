package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.RatingReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingReviewRepository extends JpaRepository<RatingReview, UUID> {

    Optional<RatingReview> findByUserAccount_Username(String username);

    // findRatingReviews from the first day of the previous month, to the last day of the previous month
    List<RatingReview> findRatingReviewByDateCreatedBetween(LocalDate ld1,
                                                                                    LocalDate ld2);

    // findRatingReviews of yesterday
    List<RatingReview> findRatingReviewsByDateCreated(LocalDate ld);
}
