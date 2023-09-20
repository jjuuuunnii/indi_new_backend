package com.indi.dev.dto.video;

import com.indi.dev.entity.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VideoReqDto {
    private File videoFile;
    private File thumbnail;
    private Genre genre;
    private String videoTitle;

    public VideoReqDto() {}
}
