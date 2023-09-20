package com.indi.dev.controller;

import com.indi.dev.dto.comment.CommentIdDto;
import com.indi.dev.dto.comment.CommentListDto;
import com.indi.dev.dto.comment.CommentReqDto;
import com.indi.dev.dto.follow.FollowStatusDto;
import com.indi.dev.dto.like.LikeStatusDto;
import com.indi.dev.dto.user.UserNickNameDto;
import com.indi.dev.dto.video.*;
import com.indi.dev.dto.view.ViewReqDto;
import com.indi.dev.entity.Genre;
import com.indi.dev.facade.AggregationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
@Slf4j
public class VideoController {

    private final AggregationFacade aggregationFacade;

    @GetMapping("/{genre}")
    public VideoListDto getVideoList(@PathVariable Genre genre) {
        return aggregationFacade.getVideoList(genre);
    }

    @PutMapping("/{genre}/{videoId}/views")
    public void putVideoViewStatus(@PathVariable Genre genre, @PathVariable Long videoId, @RequestBody ViewReqDto viewRequestDto) {
        aggregationFacade.putVideoViewStatus(genre, videoId, viewRequestDto.getNickName());
    }

    @GetMapping("/{videoId}")
    public VideoSoloInfoDto getVideoSoloInfo(@PathVariable Long videoId, @RequestParam String nickName) {
        return aggregationFacade.getVideoSoloInfo(videoId, nickName);
    }

    @GetMapping("/{videoId}/comments")
    public CommentListDto getCommentListInfo(@PathVariable Long videoId) {
        return aggregationFacade.getCommentListInfo(videoId);
    }


    @PostMapping("/{videoId}/comments")
    public void saveComment(@PathVariable Long videoId, @RequestBody CommentReqDto commentReqDto) {
        aggregationFacade.saveComment(videoId, commentReqDto.getContent(), commentReqDto.getNickName());
    }

    @DeleteMapping("/{videoId}/comments")
    public void deleteComment(@PathVariable Long videoId, @RequestBody CommentIdDto commentIdDto) {
        aggregationFacade.deleteComment(videoId, commentIdDto.getCommentId());
    }

    @PutMapping("/{videoId}/likes")
    public LikeStatusDto putLikeStatus(@PathVariable Long videoId, @RequestBody UserNickNameDto userNickNameDto) {
        return aggregationFacade.putLikeStatus(videoId, userNickNameDto.getNickName());
    }

    @PutMapping("/{videoId}/follow")
    public FollowStatusDto putFollowStatus(@PathVariable Long videoId, @RequestBody UserNickNameDto userNickNameDto) {
        return aggregationFacade.putFollowStatus(videoId, userNickNameDto.getNickName());
    }

    @GetMapping("/ranking")
    public VideoListDto getVideoRankingList(){
        return aggregationFacade.getVideoRankingList();
    }

    @PostMapping("/{nickName}")
    public void saveVideo(@ModelAttribute VideoReqDto videoReqDto, @PathVariable String nickName) {
        aggregationFacade.saveVideo(
                nickName,
                videoReqDto.getVideoFile(),
                videoReqDto.getThumbnail(),
                videoReqDto.getGenre(),
                videoReqDto.getVideoTitle()
                );
    }

    @GetMapping("/{nickName}")
    public StudioVideoListDto getStudioVideoList(@PathVariable String nickName) {
        return aggregationFacade.getStudioVideoList(nickName);
    }

    @DeleteMapping("/{nickName}")
    public void deleteVideo(@PathVariable String nickName, @RequestBody VideoIdDto videoIdDto) {
        aggregationFacade.deleteVideo(nickName, videoIdDto.getVideoIdList());
    }
}
