package com.indi.dev.facade;

import com.indi.dev.dto.comment.CommentListDto;
import com.indi.dev.dto.follow.FollowStatusDto;
import com.indi.dev.dto.like.LikeStatusDto;
import com.indi.dev.dto.video.VideoListDto;
import com.indi.dev.dto.video.VideoSoloInfoDto;
import com.indi.dev.entity.Comment;
import com.indi.dev.entity.Genre;
import com.indi.dev.entity.User;
import com.indi.dev.entity.Video;
import com.indi.dev.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AggregationFacade {
    private final UserService userService;
    private final ViewService viewService;
    private final VideoService videoService;
    private final FollowService followService;
    private final CommentService commentService;
    private final LikeService likeService;


    public void saveUserNickName(User user, String nickName) {
        userService.saveUserNickName(user,nickName);
    }




    public void putVideoViewStatus(Genre genre, Long videoId, String nickName) {
        User user = userService.findUserByNickName(nickName);
        Video video = videoService.findByVideoId(videoId);
        viewService.putVideoViewStatus(genre, video, user);
    }



    public VideoListDto getVideoList(Genre genre){
        return videoService.getVideoList(genre);
    }

    public VideoSoloInfoDto getVideoSoloInfo(Long videoId, String nickName) {
        User user = userService.findUserByNickName(nickName);
        Video video = videoService.findByVideoId(videoId);
        boolean isLiked = user.getLikes().stream()
                .anyMatch(like -> like.getVideo().getId().equals(videoId));
        boolean isFollowed = followService.checkedFollowingStatus(user, nickName);
        return videoService.getVideoSoloInfo(video, user, isLiked, isFollowed);
    }

    public CommentListDto getCommentListInfo(Long videoId) {
        Video video = videoService.findByVideoId(videoId);
        return commentService.findCommentList(video);
    }


    public void saveComment(Long videoId, String content, String nickName) {
        Video video = videoService.findByVideoId(videoId);
        User user = userService.findUserByNickName(nickName);
        commentService.saveComment(video, content, user);
    }

    public void deleteComment(Long videoId, Long commentId) {
        Comment comment = commentService.findById(commentId);
        commentService.deleteComment(comment);

    }

    public LikeStatusDto putLikeStatus(Long videoId, String nickName) {
        User user = userService.findUserByNickName(nickName);
        Video video = videoService.findByVideoId(videoId);
        return likeService.putLikeStatus(user, video);
    }

    public FollowStatusDto putFollowStatus(Long videoId, String nickName) {
        User followingUser = userService.findUserByNickName(nickName);
        Video video = videoService.findByVideoId(videoId);
        User followedUser = video.getUser();
        return followService.putFollowStatus(video, followingUser, followedUser);
    }

    public VideoListDto getVideoRankingList() {
        return videoService.getVideoRankingList();
    }
}
