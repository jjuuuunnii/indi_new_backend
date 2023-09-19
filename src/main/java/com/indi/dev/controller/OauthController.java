package com.indi.dev.controller;


import com.indi.dev.dto.oauth.UserAfterLoginDto;
import com.indi.dev.entity.SocialType;
import com.indi.dev.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
@Slf4j
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/{socialType}")
    public void redirectAuthCodeRequestUrl(
            @PathVariable SocialType socialType,
            HttpServletResponse response
    ) throws IOException{
        log.info("redirectAuthCodeRequestUrl");
        String redirectUrl = oauthService.getAuthCodeRequestUrl(socialType);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/login/{socialType}")
    ResponseEntity<UserAfterLoginDto> login(
            @PathVariable SocialType socialType,
            @RequestParam("code") String code,
            HttpServletResponse response
    ) {
        log.info("code = {} ", code);
        UserAfterLoginDto userAfterLoginDto = oauthService.login(response, socialType, code);
        return ResponseEntity.ok(userAfterLoginDto);
    }
}
