package com.indi.dev.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserMyPageInfoDto {

    private String name;
    private String email;
    private String nickName;
    private List<FollowDto> follows;
    private List<LikeDto> likes;
    public UserMyPageInfoDto(){}
}
