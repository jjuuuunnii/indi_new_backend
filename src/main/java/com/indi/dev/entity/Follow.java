package com.indi.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "follow_id_seq")
    @SequenceGenerator(name = "follow_id_seq", sequenceName = "follow_id_seq", initialValue = 1, allocationSize = 50)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_user_id")
    private User followingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_user_id")
    private User followedUser;

    private LocalDateTime createdAt;

    public static Follow makeFollowEntity(User followingUser, User followedUser) {
        Follow follow = Follow.builder()
                .followingUser(followingUser)
                .followedUser(followedUser)
                .createdAt(LocalDateTime.now())
                .build();
        followingUser.getFollowings().add(follow);
        followedUser.getFollowers().add(follow);
        return follow;
    }
    public Follow(){}
}
