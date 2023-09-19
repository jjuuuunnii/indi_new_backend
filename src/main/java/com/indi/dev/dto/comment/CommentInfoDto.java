package com.indi.dev.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentInfoDto {

    private Long commentId;
    private String nickName;
    private String content;
    private String profileImgUrl;

    public CommentInfoDto(){}
}
