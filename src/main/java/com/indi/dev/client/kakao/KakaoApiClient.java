package com.indi.dev.client.kakao;

import com.indi.dev.dto.oauth.kakao.KakaoTokenDto;
import com.indi.dev.dto.oauth.kakao.KakaoUserResponseDto;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

public interface KakaoApiClient {

    @PostExchange(url = "https://kauth.kakao.com/oauth/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    KakaoTokenDto fetchToken(@RequestParam(name ="params") LinkedMultiValueMap<String, String> params);

    @GetExchange("https://kapi.kakao.com/v2/user/me")
    KakaoUserResponseDto fetchMember(@RequestHeader(name = AUTHORIZATION) String bearerToken);
}
