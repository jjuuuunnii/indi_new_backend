package com.indi.dev.facade;

import com.indi.dev.dto.comment.CommentListDto;
import com.indi.dev.dto.follow.FollowStatusDto;
import com.indi.dev.dto.like.LikeStatusDto;
import com.indi.dev.dto.video.StudioVideoListDto;
import com.indi.dev.dto.video.VideoListDto;
import com.indi.dev.dto.video.VideoSoloInfoDto;
import com.indi.dev.entity.Comment;
import com.indi.dev.entity.Genre;
import com.indi.dev.entity.User;
import com.indi.dev.entity.Video;
import com.indi.dev.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AggregationFacade {
    private final UserService userService;
    private final ViewService viewService;
    private final VideoService videoService;
    private final FollowService followService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final S3Service s3Service;


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

    public void saveVideo(String nickName, File videoFile, File thumbnail, Genre genre, String videoTitle) {
        User user = userService.findUserByNickName(nickName);

        String videoFileName = s3Service.makeVideoFileName(videoFile.getName());
        String thumbnailName = s3Service.makeThumbnailName(thumbnail.getName());

        String videoPath = s3Service.putVideoToS3(videoFile, videoFileName);
        String thumbnailPath = s3Service.putThumbnailToS3(thumbnail, thumbnailName);
        Video video = s3Service.saveVideo(user, videoPath, thumbnailPath, genre, videoTitle);
        videoService.saveVideo(video);
    }


    public StudioVideoListDto getStudioVideoList(String nickName) {
        User user = userService.findUserByNickName(nickName);
        return videoService.getStudioVideoList(user);
    }


    public void deleteVideo(String nickName, List<Long> videoIds) {
        User user = userService.findUserByNickName(nickName);
        videoService.deleteVideo(user, videoIds);

    }

    public void putMyPageNickNameInfo(String nowNickName, String afterNickName) {
        User user = userService.findUserByNickName(nowNickName);
        userService.putMyPageNickNameInfo(user, afterNickName);
    }

    public void putMyPageProfileInfo(String nickName, File profileImg) {
        User user = userService.findUserByNickName(nickName);
        String profileName = s3Service.makeProfileName(profileImg.getName());
        String profileImgPath = s3Service.putProfileImgToS3(profileImg, profileName);
        userService.putMyPageProfileInfo(user, profileImgPath);
    }
}
