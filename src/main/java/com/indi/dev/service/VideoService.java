package com.indi.dev.service;

import com.indi.dev.dto.user.UserHomeInfoDto;
import com.indi.dev.dto.user.UserHomeInfoListDto;
import com.indi.dev.dto.video.*;
import com.indi.dev.entity.Genre;
import com.indi.dev.entity.User;
import com.indi.dev.entity.Video;
import com.indi.dev.exception.custom.CustomException;
import com.indi.dev.exception.custom.ErrorCode;
import com.indi.dev.repository.video.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;

    public VideoListDto getVideoList(Genre genre) {
        List<Video> videos = videoRepository.findByGenre(genre);
        List<VideoInfoDto> videoInfoDtos = new ArrayList<>();

        videos.stream()
                .map(video -> VideoInfoDto.builder()
                        .videoId(video.getId())
                        .videoTitle(video.getTitle())
                        .thumbnail(video.getThumbNailPath())
                        .likes(video.totalLikesCnt())
                        .nickName(video.getUser().getNickName())
                        .views(video.totalViewsCnt())
                        .profileImgUrl(video.getUser().getProfileImgUrl())
                        .build()
                )
                .forEach(videoInfoDtos::add);

        return VideoListDto.builder()
                .videoList(videoInfoDtos)
                .build();
    }


    public VideoSoloInfoDto getVideoSoloInfo(Video video, User user, boolean isLiked, boolean isFollowed) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String uploadDateTime = video.getCreatedAt().format(formatter);

        return VideoSoloInfoDto.builder()
                .videoId(video.getId())
                .videoTitle(video.getTitle())
                .views(video.totalViewsCnt())
                .nickName(user.getNickName())
                .uploadDatetime(uploadDateTime)
                .likes(video.totalLikesCnt())
                .videoUrl(video.getVideoPath())
                .likeStatus(isLiked)
                .followStatus(isFollowed)
                .animationType(video.getAnimationType())
                .totalCgImgUrl(video.getTotalCgImgPath())
                .build();

    }

    public Video findByVideoId(Long videoId) {
        return videoRepository.findById(videoId).orElseThrow(() -> new CustomException(ErrorCode.VIDEO_NOT_FOUND));
    }

    public VideoListDto getVideoRankingList() {
        List<Video> videos = videoRepository.findTop5ByLikesAndViews(PageRequest.of(0, 5));
        List<VideoInfoDto> videoInfoDtoList = videos.stream()
                .map(video -> VideoInfoDto.builder()
                        .videoId(video.getId())
                        .videoTitle(video.getTitle())
                        .thumbnail(video.getThumbNailPath())
                        .likes(video.totalLikesCnt())
                        .nickName(video.getUser().getNickName())
                        .views(video.totalViewsCnt())
                        .profileImgUrl(video.getUser().getProfileImgUrl())
                        .build()
                ).toList();

        return VideoListDto.builder()
                .videoList(videoInfoDtoList)
                .build();
    }

    @Transactional
    public void saveVideo(User user, String videoPath, String thumbnailPath, String totalCgImg, Genre genre, String videoTitle, int animationType) {
        Video video = Video.makeVideoEntity(user, videoPath, thumbnailPath, totalCgImg, genre, videoTitle, animationType);
        videoRepository.save(video);
    }

    public StudioVideoListDto getStudioVideoList(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        List<StudioVideoInfoDto> videoInfoDtoList = user.getVideos().stream()
                .map(video ->
                        StudioVideoInfoDto.builder()
                                .videoId(video.getId())
                                .thumbnail(video.getThumbNailPath())
                                .videoTitle(video.getTitle())
                                .uploadDateTime(video.getCreatedAt().format(formatter))
                                .views(video.totalViewsCnt())
                                .likes(video.totalLikesCnt())
                                .build()
                )
                .toList();

        return StudioVideoListDto.builder()
                .studioVideoList(videoInfoDtoList)
                .build();
    }

    @Transactional
    public void deleteVideo(User user, List<Long> videoIds) {
        List<Video> videos = user.getVideos();
        videos.removeIf(video -> videoIds.contains(video.getId()));
        videoIds.forEach(videoRepository::deleteById);
    }


    public UserHomeInfoListDto getUserHomeInfoList(List<Video> videos) {
        List<UserHomeInfoDto> userHomeInfoDtoList = videos.stream()
                .map(video -> UserHomeInfoDto.builder()
                        .videoId(video.getId())
                        .likes(video.totalLikesCnt())
                        .views(video.totalViewsCnt())
                        .thumbnail(video.getThumbNailPath())
                        .build()
                ).toList();

        return UserHomeInfoListDto.builder()
                .homeData(userHomeInfoDtoList)
                .build();
    }
}
