package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public ReviewDTO insert(ReviewDTO dto) {
        Review entity = new Review();
        entity.setText(entity.getText());
        entity.setMovie(entity.getMovie());
        entity.setUser(entity.getUser());
        entity = reviewRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }
}
