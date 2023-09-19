package com.indi.dev.client.naver;


import com.indi.dev.dto.oauth.naver.NaverTokenDto;
import com.indi.dev.dto.oauth.naver.NaverUserResponseDto;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


public interface NaverApiClient {

    @PostExchange(url = "https://nid.naver.com/oauth2.0/token")
    NaverTokenDto fetchToken(@RequestParam(name ="params") MultiValueMap<String, String> params);

    @GetExchange("https://openapi.naver.com/v1/nid/me")
    NaverUserResponseDto fetchUser(@RequestHeader(name = AUTHORIZATION) String bearerToken);
}
