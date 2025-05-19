package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.services.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasAnyRole('VISITOR', 'MEMBER')")
    @GetMapping
    public ResponseEntity<Page<MovieDetailsDTO>> findAll(
            @RequestParam(value = "genreId", defaultValue = "") String genreId,
            Pageable pageable) {
        Page<MovieDetailsDTO> movies = movieService.findAllPaged(genreId, pageable);
        return ResponseEntity.ok().body(movies);
    }

    @PreAuthorize("hasAnyRole('VISITOR', 'MEMBER')")
    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailsDTO> findById(@PathVariable Long id) {
        MovieDetailsDTO dto = movieService.findById(id);
        return ResponseEntity.ok().body(dto);
    }
}
