package com.hotdog.ctbs.controller.customer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotdog.ctbs.entity.RatingReview;
import com.hotdog.ctbs.repository.RatingReviewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/customer/ratings")
public class CustomerRatingsCreateController {

    private final RatingReviewRepository ratingReviewRepo;
    private final ObjectMapper objectMapper;

    public CustomerRatingsCreateController(RatingReviewRepository ratingReviewRepo)
    {
        this.ratingReviewRepo = ratingReviewRepo;
        this.objectMapper = new ObjectMapper();
    }

    @PostMapping("/create")
    public ResponseEntity<String> CreateRating(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            RatingReview.createRatingReview(
                    ratingReviewRepo,
                    jsonNode.get("username").asText(),
                    jsonNode.get("rating").asInt(),
                    jsonNode.get("review").asText()
            );
            return ResponseEntity.ok().body("Success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
