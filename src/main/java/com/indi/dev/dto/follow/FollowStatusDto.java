package com.indi.dev.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FollowStatusDto {

    private boolean followStatus;

    public FollowStatusDto(){}
}
