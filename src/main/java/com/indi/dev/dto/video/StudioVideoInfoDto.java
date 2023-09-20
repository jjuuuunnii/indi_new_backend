package com.indi.dev.dto.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class StudioVideoInfoDto {

    private Long videoId;
    private String thumbnail;
    private String videoTitle;
    private String uploadDateTime;
    private int views;
    private int likes;

    public StudioVideoInfoDto(){}
}
