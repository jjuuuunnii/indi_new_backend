package com.indi.dev.dto.comment;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentListDto {

    private List<CommentInfoDto> comments;
    public CommentListDto(){}
}
