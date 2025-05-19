package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieProjection;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import com.devsuperior.movieflix.util.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional(readOnly = true)
    public MovieDetailsDTO findById(Long id) {
        return movieRepository.findById(id).map(MovieDetailsDTO::new).orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
    }

    @Transactional(readOnly = true)
    public Page<MovieDetailsDTO> findAllPaged(String genreId, Pageable pageable) {
        List<Long> genreIds = List.of();
        if (genreId != null && !genreId.isEmpty()) {
            genreIds = Arrays.stream(genreId.split(",")).map(Long::parseLong).toList();
        }
        Page<MovieProjection> page = movieRepository.searchMovies(genreIds, pageable);
        List<Long> titlesIds = page.map(MovieProjection::getId).toList();

        List<Movie> entities = movieRepository.searchMoviesByGenre(titlesIds);
        entities = (List<Movie>) Utils.replace(page.getContent(), entities);
        List<MovieDetailsDTO> dtos = entities.stream().filter(Objects::nonNull)
                .map(MovieDetailsDTO::new).toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }
}
