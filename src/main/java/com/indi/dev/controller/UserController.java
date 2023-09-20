package com.indi.dev.controller;

import com.indi.dev.dto.user.UserNickNameDto;
import com.indi.dev.entity.PrincipalDetails;
import com.indi.dev.entity.User;
import com.indi.dev.facade.AggregationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.File;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final AggregationFacade aggregationFacade;

    @PostMapping("/moreInfo")
    public void saveUserNickName(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                 @RequestBody UserNickNameDto userNickNameDto){
        User user = principalDetails.getUser();
        aggregationFacade.saveUserNickName(user, userNickNameDto.getNickName());
    }

    @PutMapping("/{nickName}/mypage/nickName")
    public void putMyPageNickNameInfo(@PathVariable String nickName, @RequestBody UserNickNameDto userNickNameDto) {
        aggregationFacade.putMyPageNickNameInfo(nickName, userNickNameDto.getNickName());
    }

    @PutMapping("/{nickName}/mypage/nickName")
    public void putMyPageProfileInfo(@PathVariable String nickName, @RequestParam File profileImg) {
        aggregationFacade.putMyPageProfileInfo(nickName, profileImg);
    }
}
