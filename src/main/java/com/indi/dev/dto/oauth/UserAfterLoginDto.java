package com.indi.dev.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserAfterLoginDto {

    private boolean isRequiredMoreInfo;
    private String nickName;


    public UserAfterLoginDto(){}
}
