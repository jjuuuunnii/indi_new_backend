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
public class UserHomeInfoListDto {

    private List<UserHomeInfoDto> homeData;

    public UserHomeInfoListDto(){}
}
