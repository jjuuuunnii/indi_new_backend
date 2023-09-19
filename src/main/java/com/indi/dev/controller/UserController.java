package com.indi.dev.controller;

import com.indi.dev.dto.user.UserNickNameDto;
import com.indi.dev.entity.PrincipalDetails;
import com.indi.dev.entity.User;
import com.indi.dev.facade.AggregationFacade;
import com.indi.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
