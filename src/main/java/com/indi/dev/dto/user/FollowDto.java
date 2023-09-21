package com.indi.dev.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class FollowDto {

    private String profileImgUrl;
    private String nickName;

    public FollowDto(){}
}
