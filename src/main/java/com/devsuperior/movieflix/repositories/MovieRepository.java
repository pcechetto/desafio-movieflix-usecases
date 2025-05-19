package com.devsuperior.movieflix.repositories;

import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(
            nativeQuery = true,
            value = """
                    SELECT m.id, m.title
                    FROM tb_movie m
                    WHERE (:genreIds IS NULL OR m.genre_id IN (:genreIds))
                    ORDER BY m.title
                    """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM tb_movie m
                    WHERE (:genreIds IS NULL OR m.genre_id IN (:genreIds))
                    """
    )
    Page<MovieProjection> searchMovies(@Param("genreIds") List<Long> genreIds, Pageable pageable);

    @Query("SELECT obj FROM Movie obj JOIN FETCH obj.genre WHERE obj.id IN :moviesIds")
    List<Movie> searchMoviesByGenre(@Param("moviesIds") List<Long> moviesIds);
}