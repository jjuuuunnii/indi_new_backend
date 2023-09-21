package com.indi.dev.service;

import com.indi.dev.dto.follow.FollowStatusDto;
import com.indi.dev.dto.user.FollowDto;
import com.indi.dev.dto.user.LikeDto;
import com.indi.dev.dto.user.UserMyPageInfoDto;
import com.indi.dev.entity.Follow;
import com.indi.dev.entity.User;
import com.indi.dev.entity.Video;
import com.indi.dev.repository.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowService {
    private final FollowRepository followRepository;

    public List<User> findFollowingUser(User user){
        List<Follow> follows = followRepository.findByFollowingUser(user);
        List<User> followingUsers = new ArrayList<>();
        for (Follow follow : follows) {
            followingUsers.add(follow.getFollowedUser());
        }
        return followingUsers;
    }


    public boolean checkedFollowingStatus(User user, String nickName){
        List<User> followingUser = findFollowingUser(user);
        return followingUser.stream()
                .anyMatch(checkUser -> checkUser.getNickName().equals(nickName));
    }

    @Transactional
    public FollowStatusDto putFollowStatus(Video video, User followingUser, User followedUser) {
        boolean followStatus = followRepository.findByFollowingUserAndFollowedUser(followingUser, followedUser)
                .map(follow -> {
                    // 팔로우 취소: 양방향 연관관계 해제
                    followingUser.getFollowings().remove(follow);
                    followedUser.getFollowers().remove(follow);
                    followRepository.delete(follow);
                    return false;
                })
                .orElseGet(() -> {
                    // 새로운 팔로우: 양방향 연관관계 설정
                    Follow follow = Follow.makeFollowEntity(followingUser, followedUser);
                    followRepository.save(follow);
                    return true;
                });

        return FollowStatusDto.builder()
                .followStatus(followStatus)
                .build();
    }


    public List<FollowDto> getFollowDtos(List<Follow> followings) {
        return followings.stream().map(follow ->
                FollowDto.builder()
                        .profileImgUrl(follow.getFollowedUser().getProfileImgUrl())
                        .nickName(follow.getFollowedUser().getNickName())
                        .build()
        ).toList();

    }
}
