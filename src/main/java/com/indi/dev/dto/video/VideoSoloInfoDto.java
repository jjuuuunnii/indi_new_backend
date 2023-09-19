package com.indi.dev.dto.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VideoSoloInfoDto {

    private Long videoId;
    private String videoTitle;
    private int views;
    private String nickName;
    private String uploadDatetime;
    private String profileImgUrl;
    private int likes;
    private String videoUrl;
    private boolean likeStatus;
    private boolean followStatus;

    public VideoSoloInfoDto(){}
}
