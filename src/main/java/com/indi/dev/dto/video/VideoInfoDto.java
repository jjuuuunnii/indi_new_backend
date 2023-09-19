package com.indi.dev.dto.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VideoInfoDto {

    private Long videoId;
    private String videoTitle;
    private String thumbnail;
    private int likes;
    private String nickName;
    private int views;
    private String profileImgUrl;

    public VideoInfoDto(){}
}
