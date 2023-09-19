package com.indi.dev.dto.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LikeStatusDto {

    private boolean likeStatus;

    public LikeStatusDto(){}
}
