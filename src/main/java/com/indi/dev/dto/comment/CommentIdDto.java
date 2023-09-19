package com.indi.dev.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentIdDto {

    private Long commentId;

    public CommentIdDto(){}
}
