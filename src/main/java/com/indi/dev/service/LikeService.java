package com.indi.dev.service;

import com.indi.dev.dto.like.LikeStatusDto;
import com.indi.dev.dto.user.LikeDto;
import com.indi.dev.entity.Like;
import com.indi.dev.entity.User;
import com.indi.dev.entity.Video;
import com.indi.dev.repository.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public LikeStatusDto putLikeStatus(User user, Video video) {
        AtomicBoolean likeStatus = new AtomicBoolean(false);

        likeRepository.findByUserAndVideo(user, video).ifPresentOrElse(
                likeRepository::delete,
                () -> {
                    likeRepository.save(Like.makeLikeEntity(user, video));
                    likeStatus.set(true);
                }
        );

        return LikeStatusDto.builder()
                .likeStatus(likeStatus.get())
                .build();
    }

    public List<LikeDto> getLikeDtos(List<Like> likes) {
        return likes.stream().map(like ->
                LikeDto.builder()
                        .videoId(like.getVideo().getId())
                        .thumbnail(like.getVideo().getThumbNailPath())
                        .title(like.getVideo().getTitle())
                        .build()
        ).toList();
    }
}
