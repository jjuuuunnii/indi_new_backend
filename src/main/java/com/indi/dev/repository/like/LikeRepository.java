package com.indi.dev.repository.like;

import com.indi.dev.entity.Like;
import com.indi.dev.entity.User;
import com.indi.dev.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndVideo(User user, Video video);
}
