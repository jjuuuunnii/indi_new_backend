package com.indi.dev.provider;


import com.indi.dev.entity.SocialType;

public interface AuthCodeRequestUrlProvider {

    SocialType supportServer();

    String provide();
}
