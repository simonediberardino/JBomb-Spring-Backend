package com.diberardino.jbombreview.controller;

import com.diberardino.jbombreview.data.ReviewEntity;
import com.diberardino.jbombreview.domain.Review;
import com.diberardino.jbombreview.service.ReviewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for user reviews about the game in the game website.
 */
@RestController
@RequestMapping("/reviews") // Base URL path for all endpoints in this controller
public class ReviewsController {

    // The service layer that handles business logic related to reviews.
    // The controller delegates data operations to this service.
    private final ReviewsService reviewsService;

    // Constructor injection ensures that ReviewsService is provided by Spring.
    public ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    /**
     * Endpoint: GET /reviews
     *
     * This method handles requests from the frontend to retrieve all game reviews.
     * - Users visiting the website can see a list of all reviews.
     * - Delegates the fetching of reviews to ReviewsService.
     * - Returns a list of Review domain objects (not entities directly, keeping separation of concerns).
     */
    @GetMapping
    public List<Review> getAll() {
        return reviewsService.findAll();
    }

    /**
     * Endpoint: GET /reviews/author={author}/content={content}/rating={rating}
     *
     * This method allows the frontend to create a new review via the URL path.
     * - Users can submit reviews including the author name, content, and rating.
     * - Each path variable corresponds to a field in the Review.
     * - The method constructs a ReviewEntity, converts it to a domain Review, and saves it using ReviewsService.
     *
     * Note: Using GET to create resources is unconventional. Usually POST is used for creating new data.
     * This approach might be intended for testing or quick demonstration purposes.
     */
    @GetMapping("/author={author}/content={content}/rating={rating}")
    public Review createReviewFromPath(
            @PathVariable String author,
            @PathVariable String content,
            @PathVariable Integer rating
    ) {
        // Convert the incoming data into an entity, then to a domain model, and save it
        return reviewsService.save(new ReviewEntity(author, content, rating).toDomain());
    }
}
