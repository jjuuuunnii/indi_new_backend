package com.indi.dev.repository.video;

import com.indi.dev.entity.Genre;
import com.indi.dev.entity.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByGenre(Genre genre);

    @Query("SELECT v FROM Video v LEFT JOIN v.likes l LEFT JOIN v.views w " +
            "GROUP BY v " +
            "ORDER BY COUNT(l) + COUNT(w) DESC")
    List<Video> findTop5ByLikesAndViews(Pageable pageable);
}
