package com.indi.dev.client;


import com.indi.dev.entity.SocialType;
import com.indi.dev.entity.User;

public interface OauthUserClient {

    SocialType supportServer();

    User fetch(String authCode);
}
