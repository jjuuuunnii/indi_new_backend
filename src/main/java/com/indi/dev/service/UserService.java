package com.indi.dev.service;

import com.indi.dev.entity.User;
import com.indi.dev.exception.custom.CustomException;
import com.indi.dev.exception.custom.ErrorCode;
import com.indi.dev.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void saveUserNickName(User user, String nickName){
        user.updateNickName(nickName);
    }

    public User findUserByNickName(String nickName) {
        return userRepository.findByNickName(nickName).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void putMyPageNickNameInfo(User user, String afterNickName) {
        user.updateNickName(afterNickName);
    }

    @Transactional
    public void putMyPageProfileInfo(User user, String profileImgUrl) {
        user.updateProfileImgUrl(profileImgUrl);
    }
}
