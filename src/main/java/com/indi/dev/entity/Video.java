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
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "video_id_seq")
    @SequenceGenerator(name = "video_id_seq", sequenceName = "video_id_seq", initialValue = 1, allocationSize = 50)
    @Column(name = "video_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Builder.Default
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<View> views = new ArrayList<>();

    private String videoPath;
    private String thumbNailPath;
    private String title;
    private LocalDateTime createdAt;

    //private int viewsByTime;

    public Video(){}
    public int totalLikesCnt(){
        return likes.size();
    }

    public int totalViewsCnt(){
        return views.size();
    }

    public static Video makeVideoEntity(User user, String videoPath, String thumbNailPath, Genre genre, String videoTitle){
        Video video = Video.builder()
                .user(user)
                .genre(genre)
                .videoPath(videoPath)
                .thumbNailPath(thumbNailPath)
                .title(videoTitle)
                .createdAt(LocalDateTime.now())
                .build();

        user.getVideos().add(video);
        return video;
    }



}
