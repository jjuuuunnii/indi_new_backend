package com.indi.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", initialValue = 1, allocationSize = 50)
    @Column(name = "user_id")
    private Long id;
    /**
     * oauthTouser에서 받는값
     */
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    private String socialId;
    private LocalDateTime createAt;

    /**
     * TODO S3활용
     */
    private String profileImgUrl;
    private String refreshToken;

    /**
     * 추가 정보에서 받는 값
     */
    private String nickName;



    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.ALL)
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "followedUser", cascade = CascadeType.ALL)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Video> videos = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<View> views = new ArrayList<>();

    public static User oauthUserToEntity(String userName, String email, SocialType socialType, String socialId){
        return User.builder()
                .name(userName)
                .email(email)
                .socialType(socialType)
                .socialId(socialId)
                .createAt(LocalDateTime.now())
                .build();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void addFollowing(Follow follow) {
        this.followings.add(follow);
        follow.setFollowingUser(this);
    }

    public void addFollower(Follow follow) {
        this.followers.add(follow);
        follow.setFollowedUser(this);
    }

    public User(){}
}
