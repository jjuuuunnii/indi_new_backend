package com.indi.dev.facade;

import com.indi.dev.config.S3Config;
import com.indi.dev.dto.comment.CommentListDto;
import com.indi.dev.dto.follow.FollowStatusDto;
import com.indi.dev.dto.like.LikeStatusDto;
import com.indi.dev.dto.user.FollowDto;
import com.indi.dev.dto.user.LikeDto;
import com.indi.dev.dto.user.UserHomeInfoListDto;
import com.indi.dev.dto.user.UserMyPageInfoDto;
import com.indi.dev.dto.video.StudioVideoListDto;
import com.indi.dev.dto.video.VideoListDto;
import com.indi.dev.dto.video.VideoSoloInfoDto;
import com.indi.dev.entity.*;
import com.indi.dev.exception.custom.CustomException;
import com.indi.dev.exception.custom.ErrorCode;
import com.indi.dev.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

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
    private final S3Config s3Config;



    public void saveUserNickName(User user, String nickName) {
        if(userService.checkDuplicateNickName(nickName)){
            throw new CustomException(ErrorCode.DUPLICATED_USER);
        }
        userService.saveUserNickName(user,nickName);
    }




    public void putVideoViewStatus(Long videoId, String nickName) {
        User user = userService.findUserByNickName(nickName);
        Video video = videoService.findByVideoId(videoId);
        viewService.putVideoViewStatus(video, user);
    }



    public VideoListDto getVideoList(Genre genre){
        return videoService.getVideoList(genre);
    }

    public VideoSoloInfoDto getVideoSoloInfo(Long videoId) {
        Video video = videoService.findByVideoId(videoId);
        User user = video.getUser();
        boolean isLiked = user.getLikes().stream()
                .anyMatch(like -> like.getVideo().getId().equals(videoId));
        boolean isFollowed = followService.checkedFollowingStatus(user, user.getNickName());
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

    public void saveVideo(String nickName, File videoFile, File thumbnail, File totalCgImg, Genre genre, String videoTitle, int animationType) {
        User user = userService.findUserByNickName(nickName);

        String videoFileName = s3Service.makefileName("video");
        String thumbnailName = s3Service.makefileName("thumbnail");
        String totalCgImgName = s3Service.makefileName("totalCgImg");

        String videoPath = s3Service.putFileToS3(videoFile, videoFileName, s3Config.getVideoDir());
        String thumbnailPath = s3Service.putFileToS3(thumbnail, thumbnailName, s3Config.getThumbnailDir());
        String totalCgImgPath = s3Service.putFileToS3(totalCgImg, totalCgImgName, s3Config.getTotalCgImgDir());
        videoService.saveVideo(user, videoPath, thumbnailPath, totalCgImgPath, genre, videoTitle, animationType);
    }


    public StudioVideoListDto getStudioVideoList(String nickName) {
        User user = userService.findUserByNickName(nickName);
        return videoService.getStudioVideoList(user);
    }


    public void deleteVideo(String nickName, List<Long> videoIds) {
        User user = userService.findUserByNickName(nickName);
        List<Video> videos = videoIds.stream().map(videoService::findByVideoId).toList();
        List<String> thumbnailPaths = videos.stream().map(Video::getThumbNailPath).toList();

        videos.forEach(video -> s3Service.deleteFromS3(video.getVideoPath()));
        thumbnailPaths.forEach(s3Service::deleteFromS3);
        videoService.deleteVideo(user,videoIds);
    }

    public void putMyPageNickNameInfo(String nowNickName, String afterNickName) {
        User user = userService.findUserByNickName(nowNickName);
        userService.putMyPageNickNameInfo(user, afterNickName);
    }

    public void putMyPageProfileInfo(String nickName, File profileImg) {
        User user = userService.findUserByNickName(nickName);
        if(!user.getProfileImgUrl().equals(s3Config.getDefaultImgPath())){
            s3Service.deleteFromS3(user.getProfileImgUrl());
        }
        String profileName = s3Service.makefileName("profile");
        String profileImgPath = s3Service.putFileToS3(profileImg, profileName, s3Config.getProfileImgDir());
        userService.putMyPageProfileInfo(user, profileImgPath);
    }

    public UserHomeInfoListDto getUserHomeInfoList(String nickName) {
        User user = userService.findUserByNickName(nickName);
        return videoService.getUserHomeInfoList(user.getVideos());
    }

    public UserMyPageInfoDto getUserMyPageInfo(String nickName) {
        User user = userService.findUserByNickName(nickName);
        List<Follow> followings = user.getFollowings();
        List<Like> likes = user.getLikes();

        List<FollowDto> followDtos = followService.getFollowDtos(followings);
        List<LikeDto> likeDtos = likeService.getLikeDtos(likes);
        return userService.getMyPageInfo(user, followDtos, likeDtos);
    }
}
