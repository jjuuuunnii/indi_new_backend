package com.indi.dev.service;

import com.indi.dev.dto.user.FollowDto;
import com.indi.dev.dto.user.LikeDto;
import com.indi.dev.dto.user.UserMyPageInfoDto;
import com.indi.dev.entity.User;
import com.indi.dev.exception.custom.CustomException;
import com.indi.dev.exception.custom.ErrorCode;
import com.indi.dev.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void saveUserNickName(User principalUser, String nickName){
        User user = userRepository.findById(principalUser.getId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
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


    public UserMyPageInfoDto getMyPageInfo(User user, List<FollowDto> followDtos, List<LikeDto> likeDtos) {
        return UserMyPageInfoDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .follows(followDtos)
                .likes(likeDtos)
                .build();
    }

    public boolean checkDuplicateNickName(String nickName) {
        return userRepository.existsByNickName(nickName);
    }

}
