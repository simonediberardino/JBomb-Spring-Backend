package com.diberardino.jbombreview.service;

import com.diberardino.jbombreview.data.ReviewEntity;
import com.diberardino.jbombreview.domain.Review;
import com.diberardino.jbombreview.repository.ReviewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewsService {
    private final ReviewsRepository reviewsRepository;

    public ReviewsService(ReviewsRepository reviewsRepository) {
        this.reviewsRepository = reviewsRepository;
    }

    public List<Review> findAll() {
        return reviewsRepository.findAll().stream().map(ReviewEntity::toDomain).toList();
    }

    public Review findById(Long id) {
        Optional<ReviewEntity> reviewEntityOptional = reviewsRepository.findById(id);
        return reviewEntityOptional.map(ReviewEntity::toDomain).orElse(null);
    }

    public Review save(Review review) {
        return reviewsRepository.save(review.toEntity()).toDomain();
    }
}
