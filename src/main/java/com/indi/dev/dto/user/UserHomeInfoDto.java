package com.indi.dev.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserHomeInfoDto {

    private String thumbnail;
    private Long videoId;
    private int likes;
    private int views;

    public UserHomeInfoDto(){}
}
