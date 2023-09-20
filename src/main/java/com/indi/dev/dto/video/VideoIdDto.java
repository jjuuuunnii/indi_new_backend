package com.indi.dev.dto.video;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VideoIdDto {

    private List<Long> videoIdList;

    public VideoIdDto(){}
}
