package com.indi.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "view_id_seq")
    @SequenceGenerator(name = "view_id_seq", sequenceName = "view_id_seq", initialValue = 1, allocationSize = 50)
    @Column(name = "view_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="video_id")
    private Video video;
    private LocalDateTime createdAt;

    public static View makeViewEntity(Video video, User user){
        View view = View.builder()
                .video(video)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        video.getViews().add(view);
        user.getViews().add(view);

        return view;
    }
    public View(){}


}
