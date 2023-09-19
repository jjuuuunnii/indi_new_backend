package com.indi.dev.exception.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info("request.getRequestURI() = {} ", request.getRequestURI());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();

        if (authException instanceof InvalidAccessTokenException) {
            InvalidAccessTokenException ex = (InvalidAccessTokenException) authException;
            jsonObject.put("message", ex.getMessage());
            jsonObject.put("code", ex.getErrorCode());
        } else {
            jsonObject.put("message", authException.getMessage());
            jsonObject.put("code", "UNKNOWN_ERROR");
        }
        response.getWriter().write(jsonObject.toString());

        log.error("error = {}", authException.getMessage());
    }
}

