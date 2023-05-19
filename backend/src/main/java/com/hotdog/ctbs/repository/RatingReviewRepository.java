package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.RatingReview;
import com.hotdog.ctbs.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingReviewRepository extends JpaRepository<RatingReview, UUID> {

    // findRatingReviews from the first day of the previous month, to the last day of the previous month
    List<RatingReview> findRatingReviewByDateCreatedBetween(LocalDate ld1,
                                                            LocalDate ld2);

    @Query("select u from UserAccount u where u.username = ?1")
    Optional<UserAccount> findUserAccountByUsername(String username);
}
