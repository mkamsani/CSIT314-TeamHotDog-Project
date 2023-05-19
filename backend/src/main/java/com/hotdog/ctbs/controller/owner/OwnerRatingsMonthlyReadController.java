package com.hotdog.ctbs.controller.owner;

import com.hotdog.ctbs.entity.RatingReview;
import com.hotdog.ctbs.repository.RatingReviewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/owner/ratings/monthly")
public class OwnerRatingsMonthlyReadController {
    private final RatingReviewRepository ratingReviewRepo;

    public OwnerRatingsMonthlyReadController(RatingReviewRepository ratingReviewRepo)
    {
        this.ratingReviewRepo = ratingReviewRepo;
    }

    @GetMapping(value = "/read")
    public ResponseEntity<String> readRatingReviewLastMonth()
    {
        try {
            String json = RatingReview.readRatingReview(ratingReviewRepo, "lastMonth");
            return ResponseEntity.ok().body(json);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
