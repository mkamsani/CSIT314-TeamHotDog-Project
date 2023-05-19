package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.RatingReviewRepository;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "rating_review")
public class RatingReview {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uuid", nullable = false)
    protected UUID id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_account", nullable = false)
    protected UserAccount userAccount;

    @Column(name = "rating", nullable = false)
    protected Integer rating;

    @Column(name = "review", nullable = false, length = Integer.MAX_VALUE)
    protected String review;

    @Column(name = "date_created", nullable = false)
    protected LocalDate dateCreated;

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    //////////////////////////////// Service /////////////////////////////////

    public static String readRatingReview(RatingReviewRepository ratingReviewRepo,
                                          String when)
    {
        List<RatingReview> ratingReviews = switch (when) {
            case "lastMonth" -> {
                LocalDate lastMonth = LocalDate.now().minusMonths(1);
                yield ratingReviewRepo.findRatingReviewByDateCreatedBetween(
                        lastMonth.withDayOfMonth(1),
                        lastMonth.withDayOfMonth(lastMonth.lengthOfMonth()));
            }
            case "lastWeek" -> {
                LocalDate lastWeek = LocalDate.now().minusWeeks(1);
                yield ratingReviewRepo.findRatingReviewByDateCreatedBetween(
                        lastWeek.with(java.time.DayOfWeek.MONDAY),
                        lastWeek.with(java.time.DayOfWeek.SUNDAY));
            }
            case "yesterday" -> {
                LocalDate yesterday = LocalDate.now().minusDays(1);
                yield ratingReviewRepo.findRatingReviewsByDateCreated(yesterday);
            }
            default -> throw new IllegalStateException("Unexpected value: " + when);
        };

        ratingReviews.sort((rr1, rr2) -> {
            int dateCreatedCompare = rr1.dateCreated.compareTo(rr2.dateCreated);
            if (dateCreatedCompare != 0) return dateCreatedCompare;
            int ratingCompare = rr1.rating.compareTo(rr2.rating);
            if (ratingCompare != 0) return ratingCompare;
            return rr1.userAccount.username.compareTo(rr2.userAccount.username);
        });

        ArrayNode an = objectMapper.createArrayNode();
        for (RatingReview rr : ratingReviews) {
            ObjectNode on = objectMapper.createObjectNode();
            on.put("username", rr.userAccount.username);
            on.put("rating", rr.rating);
            on.put("review", rr.review);
            on.put("dateCreated", rr.dateCreated.toString());
            an.add(on);
        }
        return an.toString();
    }
}