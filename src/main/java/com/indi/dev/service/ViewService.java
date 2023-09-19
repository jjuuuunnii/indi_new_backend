package com.indi.dev.service;

import com.indi.dev.entity.Genre;
import com.indi.dev.entity.User;
import com.indi.dev.entity.Video;
import com.indi.dev.entity.View;
import com.indi.dev.repository.view.ViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ViewService {
    private final ViewRepository viewRepository;

    @Transactional
    public void putVideoViewStatus(Genre genre, Video video, User user) {
        View view = View.makeViewEntity(video, user);
        viewRepository.save(view);
    }
}
