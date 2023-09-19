package com.indi.dev.client.google;


import com.indi.dev.dto.oauth.google.GoogleTokenDto;
import com.indi.dev.dto.oauth.google.GoogleUserResponseDto;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

public interface GoogleApiClient {


    @PostExchange(url = "https://oauth2.googleapis.com/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    GoogleTokenDto fetchToken(@RequestParam(name = "params") MultiValueMap<String, String> params);

    @GetExchange(url = "https://www.googleapis.com/oauth2/v2/userinfo")
    GoogleUserResponseDto fetchUser(@RequestHeader(name = AUTHORIZATION) String bearerToken);
}
