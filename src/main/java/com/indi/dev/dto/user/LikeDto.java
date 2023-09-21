package com.indi.dev.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LikeDto {

    private Long videoId;
    private String thumbnail;
    private String title;

    public LikeDto(){}
}
