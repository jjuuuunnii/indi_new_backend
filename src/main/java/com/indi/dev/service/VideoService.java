package com.indi.dev.service;

import com.indi.dev.repository.video.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;
}
