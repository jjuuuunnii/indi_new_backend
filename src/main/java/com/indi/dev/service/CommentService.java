package com.indi.dev.service;

import com.indi.dev.dto.comment.CommentInfoDto;
import com.indi.dev.dto.comment.CommentListDto;
import com.indi.dev.entity.Comment;
import com.indi.dev.entity.User;
import com.indi.dev.entity.Video;
import com.indi.dev.exception.custom.CustomException;
import com.indi.dev.exception.custom.ErrorCode;
import com.indi.dev.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentListDto findCommentList(Video video) {
        List<Comment> comments = video.getComments();

        List<CommentInfoDto> commentInfoList = comments.stream()
                .map(comment -> CommentInfoDto.builder()
                        .commentId(comment.getId())
                        .nickName(comment.getUser().getNickName())
                        .content(comment.getContent())
                        .profileImgUrl(comment.getUser().getProfileImgUrl())
                        .build())
                .toList();

        return CommentListDto.builder()
                .comments(commentInfoList)
                .build();
    }

    @Transactional
    public void saveComment(Video video, String content, User user) {
        Comment comment = Comment.saveComment(video, content, user);
        commentRepository.save(comment);
    }

    public Comment findById(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    @Transactional
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
